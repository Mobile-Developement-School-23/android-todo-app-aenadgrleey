package com.aenadgrleey.tododomain.repository

import com.aenadgrleey.core.domain.NetworkStatus
import com.aenadgrleey.core.domain.models.TodoItemData
import kotlinx.coroutines.flow.Flow

/*
Repository for giving data to ui layer
 */
interface TodoItemRepository {
    fun todoItems(includeCompleted: Boolean): Flow<List<TodoItemData>>

    fun completedItemsCount(): Flow<Int>

    val networkStatus: Flow<NetworkStatus>

    suspend fun addTodoItem(todoItem: TodoItemData)

    suspend fun deleteTodoItem(todoItem: TodoItemData)

    suspend fun fetchRemoteData()
}