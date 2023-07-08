package com.aenadgrleey.tobedone.data.repositories

import com.aenadgrleey.tobedone.data.datasources.TodoItemsLocalDataSource
import com.aenadgrleey.tobedone.data.datasources.TodoItemsRemoteDataSource
import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.aenadgrleey.tobedone.data.network.NetworkStatus
import com.aenadgrleey.tobedone.data.network.exceptions.NetworkCodeToExceptionTransformator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
    private val localDataSource: TodoItemsLocalDataSource,
    private val remoteDataSource: TodoItemsRemoteDataSource
) : TodoItemRepository {

    private val networkStatusChannel = Channel<NetworkStatus>()

    private val networkHandler = NetworkCodeToExceptionTransformator(
        onConnectionAbsence = { networkStatusChannel.send(NetworkStatus.NO_INTERNET) },
        onServerError = { networkStatusChannel.send(NetworkStatus.SERVER_INTERNAL_ERROR) },
        onBadAuth = { networkStatusChannel.send(NetworkStatus.SERVER_ERROR) },
        onElementAbsence = { networkStatusChannel.send(NetworkStatus.SERVER_ERROR) },
        onBadSync = {
            fetchRemoteData()
            it.invoke()
        },
        onFinal = { networkStatusChannel.send(NetworkStatus.PENDING) }
    )

    override fun todoItems(includeCompleted: Boolean): Flow<List<TodoItemData>> =
        localDataSource.getTodoItems(includeCompleted)

    override fun completedItemsCount(): Flow<Int> =
        localDataSource.completedItemsCount()

    override val networkStatus = networkStatusChannel.receiveAsFlow()

    override suspend fun addTodoItem(todoItem: TodoItemData) = withContext(Dispatchers.IO) {
        todoItem.lastModified = Calendar.getInstance().time
        localDataSource.addTodoItem(todoItem)
        networkHandler.tryRemote {
            remoteDataSource.addTodoItem(todoItem)
        }
    }

    override suspend fun deleteTodoItem(todoItem: TodoItemData) = withContext(Dispatchers.IO) {
        localDataSource.deleteTodoItem(todoItem)
        networkHandler.tryRemote { remoteDataSource.deleteTodoItem(todoItem) }
    }

    override suspend fun fetchRemoteData() {
        withContext(Dispatchers.IO) {
            networkStatusChannel.send(NetworkStatus.SYNCING)
            networkHandler.tryRemote {
                remoteDataSource.getTodoItems().run {
                    localDataSource.clearDatabase()
                    forEach {
                        localDataSource.addTodoItem(it)
                    }
                }
                networkStatusChannel.send(NetworkStatus.SYNCED)
            }
        }
    }
}