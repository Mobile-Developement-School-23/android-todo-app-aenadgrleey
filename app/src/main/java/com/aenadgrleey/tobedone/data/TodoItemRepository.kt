package com.aenadgrleey.tobedone.data

import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {
    fun todoItems(includeCompleted: Boolean): Flow<List<TodoItemData>>

    fun completedItemsCount(): Flow<Int>

    suspend fun addTodoItem(todoItem: TodoItemData)

    suspend fun deleteTodoItem(todoItem: TodoItemData)
}