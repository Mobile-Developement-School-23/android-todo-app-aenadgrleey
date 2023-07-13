package com.aenadgrleey.repositories

import com.aenadgrleey.data.remote.NetworkStatus
import kotlinx.coroutines.flow.Flow

/*
Repository for giving data to ui layer
 */
interface TodoItemRepository {
    fun todoItems(includeCompleted: Boolean): Flow<List<com.aenadgrleey.data.models.TodoItemData>>

    fun completedItemsCount(): Flow<Int>

    val networkStatus: Flow<NetworkStatus>

    suspend fun addTodoItem(todoItem: com.aenadgrleey.data.models.TodoItemData)

    suspend fun deleteTodoItem(todoItem: com.aenadgrleey.data.models.TodoItemData)

    suspend fun fetchRemoteData()
}