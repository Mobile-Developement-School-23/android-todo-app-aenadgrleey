package com.aenadgrleey.local

import com.aenadgrleey.data.models.TodoItemData
import kotlinx.coroutines.flow.Flow

/*
Local todos storage interface
 */
interface TodoItemsLocalDataSource {
    fun getTodoItems(excludeCompleted: Boolean): Flow<List<TodoItemData>>

    fun getTodoItems(): List<TodoItemData>

    fun clearDatabase()

    fun completedItemsCount(): Flow<Int>

    suspend fun addTodoItem(todoItem: TodoItemData)

    suspend fun deleteTodoItem(todoItem: TodoItemData)

}