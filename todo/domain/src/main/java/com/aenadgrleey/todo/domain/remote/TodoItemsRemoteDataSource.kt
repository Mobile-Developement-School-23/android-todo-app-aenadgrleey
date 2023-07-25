package com.aenadgrleey.todo.domain.remote

import com.aenadgrleey.todo.domain.models.TodoItemData


/*
Remote client interface to access todos
 */
interface TodoItemsRemoteDataSource {
    suspend fun getTodoItems(): List<TodoItemData>

    suspend fun addTodoItems(items: List<TodoItemData>): List<TodoItemData>

    suspend fun addTodoItem(item: TodoItemData): TodoItemData

    suspend fun deleteTodoItem(item: TodoItemData)
}