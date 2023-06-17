package com.aenadgrleey.tobedone.data

import kotlinx.coroutines.flow.Flow

interface TodoItemsDataSource {
    fun getTodoItems(includeCompleted: Boolean = false): Flow<List<TodoItemData>>

    fun completedItemsCount(): Flow<Int>

    suspend fun addTodoItem(item: TodoItemData)

    suspend fun deleteTodoItem(item: TodoItemData)
}