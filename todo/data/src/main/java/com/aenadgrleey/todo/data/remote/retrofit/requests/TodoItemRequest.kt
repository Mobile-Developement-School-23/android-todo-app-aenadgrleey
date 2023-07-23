package com.aenadgrleey.todo.data.remote.retrofit.requests

import com.aenadgrleey.todo.data.remote.models.TodoItemDataNetwork
import com.google.gson.annotations.SerializedName

internal class TodoItemRequest(
    @SerializedName("element")
    val item: TodoItemDataNetwork,
)