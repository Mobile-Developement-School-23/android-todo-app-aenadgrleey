package com.aenadgrleey.todo.data.remote.retrofit.responses

import com.aenadgrleey.todo.data.remote.models.TodoItemDataNetwork
import com.google.gson.annotations.SerializedName

internal class TodoItemResponse(
    @SerializedName("element")
    val item: TodoItemDataNetwork,
    @SerializedName("revision")
    val revision: Int,
)