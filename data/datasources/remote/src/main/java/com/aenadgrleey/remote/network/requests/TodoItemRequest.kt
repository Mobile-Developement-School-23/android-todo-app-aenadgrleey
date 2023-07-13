package com.aenadgrleey.remote.network.requests

import com.google.gson.annotations.SerializedName

internal class TodoItemRequest(
    @SerializedName("element")
    val item: com.aenadgrleey.data.models.TodoItemData,
)