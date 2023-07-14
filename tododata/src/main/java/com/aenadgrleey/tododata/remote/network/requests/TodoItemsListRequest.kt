package com.aenadgrleey.tododata.remote.network.requests

import com.google.gson.annotations.SerializedName

internal class TodoItemsListRequest(
    @SerializedName("list")
    val todoItemsList: List<com.aenadgrleey.core.domain.models.TodoItemData>,
)