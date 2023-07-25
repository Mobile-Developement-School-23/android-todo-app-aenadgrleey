package com.aenadgrleey.todo.data.remote.retrofit.responses

import com.aenadgrleey.todo.data.remote.models.TodoItemDataNetwork
import com.google.gson.annotations.SerializedName

internal class TodoItemsListResponse(
    @SerializedName("list")
    val todoItemsList: List<TodoItemDataNetwork>,
    @SerializedName("revision")
    val revision: Int,
)