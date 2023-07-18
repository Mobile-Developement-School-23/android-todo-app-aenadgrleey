package com.aenadgrleey.todo.data.remote.network.responses

import com.aenadgrleey.todo.domain.models.TodoItemData
import com.google.gson.annotations.SerializedName

internal class TodoItemResponse(
    @SerializedName("element")
    val item: TodoItemData,
    @SerializedName("revision")
    val revision: Int,
)