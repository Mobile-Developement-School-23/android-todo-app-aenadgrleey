package com.aenadgrleey.tobedone.data

import kotlinx.coroutines.flow.Flow

interface TodoItemsDataSource {

    suspend fun addTodoItem(item: TodoItemData)

    fun getTodoItems(): Flow<List<TodoItemData>>

    suspend fun deleteTodoItem(item: TodoItemData)
}