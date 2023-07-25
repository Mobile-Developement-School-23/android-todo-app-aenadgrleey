package com.aenadgrleey.todo.data.remote.retrofit.requests

import com.aenadgrleey.todo.data.remote.models.TodoItemDataNetwork
import com.google.gson.annotations.SerializedName

internal class TodoItemsListRequest(
    @SerializedName("list")
    val todoItemsList: List<TodoItemDataNetwork>,
)