package com.aenadgrleey.todo.data.repository

import android.util.Log
import com.aenadgrleey.auth.domain.AuthProvider
import com.aenadgrleey.core.data.remote.exceptions.DifferentRevisionsException
import com.aenadgrleey.core.data.remote.exceptions.NoSuchElementOnServerException
import com.aenadgrleey.core.data.remote.exceptions.ServerErrorException
import com.aenadgrleey.core.data.remote.exceptions.WrongAuthorizationException
import com.aenadgrleey.todo.domain.local.TodoItemsLocalDataSource
import com.aenadgrleey.todo.domain.models.NetworkStatus
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todo.domain.remote.TodoItemsRemoteDataSource
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import com.aenadgrleey.todonotify.domain.TodoNotificationDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

/*
Implementation of the repository for giving data to ui layer
 */
class TodoItemsRepositoryImpl @Inject constructor(
    private val notificationDispatcher: TodoNotificationDispatcher,
    private val authProvider: AuthProvider,
    private val localDataSource: TodoItemsLocalDataSource,
    private val remoteDataSource: TodoItemsRemoteDataSource,
) : TodoItemRepository {


    override fun todoItems(includeCompleted: Boolean): Flow<List<TodoItemData>> =
        localDataSource.getTodoItems(includeCompleted)

    override suspend fun todoItem(id: String): TodoItemData? = localDataSource.todoItem(id)

    override fun completedItemsCount(): Flow<Int> =
        localDataSource.completedItemsCount()

    private val networkStatusChannel = Channel<NetworkStatus>()
    override val networkStatus = networkStatusChannel.receiveAsFlow()

    override suspend fun addTodoItem(todoItem: TodoItemData) = withContext(Dispatchers.IO) {
        if (todoItem.id == null) todoItem.id = UUID.randomUUID().toString()
        if (todoItem.created == null) todoItem.created = Calendar.getInstance().time
        todoItem.lastModified = Calendar.getInstance().time
        todoItem.lastModifiedBy = authProvider.authInfo().deviceId
        localDataSource.addTodoItem(todoItem)
        notificationDispatcher.handleTodo(todoItem)
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
                    forEach {
                        localDataSource.addTodoItem(it)
                        notificationDispatcher.handleTodo(it)
                    }
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
    }
}