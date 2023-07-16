package com.aenadgrleey.tododomain.remote


/*
Remote client interface to access todos
 */
interface TodoItemsRemoteDataSource {
    suspend fun getTodoItems(): List<com.aenadgrleey.core.domain.models.TodoItemData>

    suspend fun addTodoItems(items: List<com.aenadgrleey.core.domain.models.TodoItemData>): List<com.aenadgrleey.core.domain.models.TodoItemData>

    suspend fun addTodoItem(item: com.aenadgrleey.core.domain.models.TodoItemData): com.aenadgrleey.core.domain.models.TodoItemData

    suspend fun deleteTodoItem(item: com.aenadgrleey.core.domain.models.TodoItemData)
}