package com.aenadgrleey.tobedone.data.datasources

import com.aenadgrleey.tobedone.data.models.TodoItemData

interface TodoItemsRemoteDataSource {
    suspend fun getTodoItems(): List<TodoItemData>

    suspend fun addTodoItems(items: List<TodoItemData>): List<TodoItemData>

    suspend fun addTodoItem(item: TodoItemData): TodoItemData

    suspend fun deleteTodoItem(item: TodoItemData)
}