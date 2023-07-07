package com.aenadgrleey.tobedone.data.repositories

import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.aenadgrleey.tobedone.data.network.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {
    fun todoItems(includeCompleted: Boolean): Flow<List<TodoItemData>>

    fun completedItemsCount(): Flow<Int>

    val networkStatus: Flow<NetworkStatus>

    suspend fun addTodoItem(todoItem: TodoItemData)

    suspend fun deleteTodoItem(todoItem: TodoItemData)

    suspend fun fetchRemoteData()
}