package com.aenadgrleey.repositories

import android.util.Log
import com.aenadgrleey.data.remote.NetworkStatus
import com.aenadgrleey.data.remote.exceptions.DifferentRevisionsException
import com.aenadgrleey.data.remote.exceptions.NoSuchElementOnServerException
import com.aenadgrleey.data.remote.exceptions.ServerErrorException
import com.aenadgrleey.data.remote.exceptions.WrongAuthorizationException
import com.aenadgrleey.local.TodoItemsLocalDataSource
import com.aenadgrleey.remote.TodoItemsRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

/*
Implementation of the repository for giving data to ui layer
 */
class TodoItemsRepositoryImpl @Inject constructor(
    private val localDataSource: TodoItemsLocalDataSource,
    private val remoteDataSource: TodoItemsRemoteDataSource,
) : TodoItemRepository {


    override fun todoItems(includeCompleted: Boolean): Flow<List<com.aenadgrleey.data.models.TodoItemData>> =
        localDataSource.getTodoItems(includeCompleted)

    override fun completedItemsCount(): Flow<Int> =
        localDataSource.completedItemsCount()

    private val networkStatusChannel = Channel<NetworkStatus>()
    override val networkStatus = networkStatusChannel.receiveAsFlow()

    override suspend fun addTodoItem(todoItem: com.aenadgrleey.data.models.TodoItemData) = withContext(Dispatchers.IO) {
        todoItem.lastModified = Calendar.getInstance().time
        localDataSource.addTodoItem(todoItem)
        tryRemote { remoteDataSource.addTodoItem(todoItem) }
    }

    override suspend fun deleteTodoItem(todoItem: com.aenadgrleey.data.models.TodoItemData) = withContext(Dispatchers.IO) {
        localDataSource.deleteTodoItem(todoItem)
        tryRemote { remoteDataSource.deleteTodoItem(todoItem) }
    }

    override suspend fun fetchRemoteData() {
        withContext(Dispatchers.IO) {
            networkStatusChannel.send(NetworkStatus.SYNCING)
            tryRemote {
                remoteDataSource.getTodoItems().run {
                    localDataSource.clearDatabase()
                    forEach { localDataSource.addTodoItem(it) }
                }
                networkStatusChannel.send(NetworkStatus.SYNCED)
            }
        }
    }

    private suspend fun tryRemote(block: suspend () -> Unit) = try {
        block.invoke()
    } catch (unknownHostException: java.net.UnknownHostException) {
        Log.e("NetworkError", unknownHostException.toString())
        networkStatusChannel.send(NetworkStatus.NO_INTERNET)
    } catch (serverErrorException: ServerErrorException) {
        Log.e("NetworkError", serverErrorException.toString())
        networkStatusChannel.send(NetworkStatus.SERVER_INTERNAL_ERROR)
    } catch (wrongAuthorization: WrongAuthorizationException) {
        Log.e("NetworkError", wrongAuthorization.toString())
        networkStatusChannel.send(NetworkStatus.SERVER_ERROR)
    } catch (noSuchElementOnServerException: NoSuchElementOnServerException) {
        Log.e("NetworkError", noSuchElementOnServerException.toString())
        networkStatusChannel.send(NetworkStatus.SERVER_ERROR)
    } catch (unsynchronizedDataException: DifferentRevisionsException) {
        Log.e("NetworkError", unsynchronizedDataException.toString())
        fetchRemoteData()
        block.invoke()
    } finally {
        networkStatusChannel.send(NetworkStatus.PENDING)
    }
}