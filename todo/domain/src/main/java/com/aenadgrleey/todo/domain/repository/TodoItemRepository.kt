package com.aenadgrleey.todo.domain.repository

import com.aenadgrleey.todo.domain.models.TodoItemData
import kotlinx.coroutines.flow.Flow

/*
Repository for giving data to ui layer
 */
interface TodoItemRepository {
    fun todoItems(includeCompleted: Boolean): Flow<List<TodoItemData>>

    suspend fun todoItem(id: String): TodoItemData?

    fun completedItemsCount(): Flow<Int>

    suspend fun addTodoItem(todoItem: TodoItemData)

    suspend fun deleteTodoItem(todoItem: TodoItemData)

    suspend fun fetchRemoteData()
}