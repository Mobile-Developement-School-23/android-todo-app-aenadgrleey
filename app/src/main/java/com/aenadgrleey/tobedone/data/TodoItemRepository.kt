package com.aenadgrleey.tobedone.data

import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {
    fun todoItems(): Flow<List<TodoItemData>>

    suspend fun addTodoItem(todoItem: TodoItemData)

    suspend fun deleteTodoItem(todoItem: TodoItemData)
}