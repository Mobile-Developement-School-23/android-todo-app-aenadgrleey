package com.aenadgrleey.tododata.remote.network.requests

import com.google.gson.annotations.SerializedName

internal class TodoItemRequest(
    @SerializedName("element")
    val item: com.aenadgrleey.core.domain.models.TodoItemData,
)