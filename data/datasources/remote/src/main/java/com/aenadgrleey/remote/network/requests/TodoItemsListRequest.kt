package com.aenadgrleey.remote.network.requests

import com.google.gson.annotations.SerializedName

internal class TodoItemsListRequest(
    @SerializedName("list")
    val todoItemsList: List<com.aenadgrleey.data.models.TodoItemData>,
)