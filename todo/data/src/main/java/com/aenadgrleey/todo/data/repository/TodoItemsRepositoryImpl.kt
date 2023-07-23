package com.aenadgrleey.todo.data.repository

import android.util.Log
import com.aenadgrleey.auth.domain.AuthProvider
import com.aenadgrleey.core.domain.exceptions.DifferentRevisionsException
import com.aenadgrleey.todo.domain.local.TodoItemsLocalDataSource
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todo.domain.remote.TodoItemsRemoteDataSource
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import com.aenadgrleey.todonotify.domain.TodoNotificationDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
            tryRemote {
                remoteDataSource.getTodoItems().run {
                    localDataSource.clearDatabase()
                    forEach {
                        localDataSource.addTodoItem(it)
                        notificationDispatcher.handleTodo(it)
                    }
                }
            }
        }
    }

    private suspend fun tryRemote(block: suspend () -> Unit): Unit = try {
        block.invoke()
    } catch (unsynchronizedDataException: DifferentRevisionsException) {
        Log.e("NetworkError", unsynchronizedDataException.toString())
        fetchRemoteData()
        block.invoke()
    }
}