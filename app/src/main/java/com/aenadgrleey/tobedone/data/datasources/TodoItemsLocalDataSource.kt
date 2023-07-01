package com.aenadgrleey.tobedone.data.datasources

import com.aenadgrleey.tobedone.data.models.TodoItemData
import kotlinx.coroutines.flow.Flow

interface TodoItemsLocalDataSource {
    fun getTodoItems(excludeCompleted: Boolean): Flow<List<TodoItemData>>

    fun getLocalTodoItems(): List<TodoItemData>

    fun clearDatabase()

    fun completedItemsCount(): Flow<Int>

    suspend fun addTodoItem(todoItem: TodoItemData)

    suspend fun deleteTodoItem(todoItem: TodoItemData)

}