package com.aenadgrleey.tobedone.data.repositories

import android.util.Log
import com.aenadgrleey.tobedone.data.datasources.TodoItemsLocalDataSource
import com.aenadgrleey.tobedone.data.datasources.TodoItemsRemoteDataSource
import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.aenadgrleey.tobedone.data.network.NetworkStatus
import com.aenadgrleey.tobedone.data.network.exceptions.DifferentRevisionsException
import com.aenadgrleey.tobedone.data.network.exceptions.NoSuchElementException
import com.aenadgrleey.tobedone.data.network.exceptions.ServerErrorException
import com.aenadgrleey.tobedone.data.network.exceptions.WrongAuthorizationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val localDataSource: TodoItemsLocalDataSource,
    private val remoteDataSource: TodoItemsRemoteDataSource,
) : TodoItemRepository {

    private val networkStatusChannel = Channel<NetworkStatus>()


    override fun todoItems(includeCompleted: Boolean): Flow<List<TodoItemData>> =
        localDataSource.getTodoItems(includeCompleted)

    override fun completedItemsCount(): Flow<Int> =
        localDataSource.completedItemsCount()

    override val networkStatus = networkStatusChannel.receiveAsFlow()

    override suspend fun addTodoItem(todoItem: TodoItemData) = withContext(Dispatchers.IO) {
        todoItem.lastModified = Calendar.getInstance().time
        localDataSource.addTodoItem(todoItem)
        tryRemote { remoteDataSource.addTodoItem(todoItem) }
    }

    override suspend fun deleteTodoItem(todoItem: TodoItemData) = withContext(Dispatchers.IO) {
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

    private suspend fun tryRemote(block: suspend () -> Unit) {
        try {
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
        } catch (noSuchElementException: NoSuchElementException) {
            Log.e("NetworkError", noSuchElementException.toString())
            networkStatusChannel.send(NetworkStatus.SERVER_ERROR)
        } catch (unsynchronizedDataException: DifferentRevisionsException) {
            Log.e("NetworkError", unsynchronizedDataException.toString())
            fetchRemoteData()
            block.invoke()
        } finally {
            networkStatusChannel.send(NetworkStatus.PENDING)
        }
    }
}