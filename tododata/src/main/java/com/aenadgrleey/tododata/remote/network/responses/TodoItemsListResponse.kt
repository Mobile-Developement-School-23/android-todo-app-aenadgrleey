package com.aenadgrleey.tododata.remote.network.responses

import com.google.gson.annotations.SerializedName

internal class TodoItemsListResponse(
    @SerializedName("list")
    val todoItemsList: List<com.aenadgrleey.core.domain.models.TodoItemData>,
    @SerializedName("revision")
    val revision: Int,
)