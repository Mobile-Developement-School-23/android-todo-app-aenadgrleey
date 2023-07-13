package com.aenadgrleey.remote.network.responses

import com.aenadgrleey.data.models.TodoItemData
import com.google.gson.annotations.SerializedName

internal class TodoItemsListResponse(
    @SerializedName("list")
    val todoItemsList: List<com.aenadgrleey.data.models.TodoItemData>,
    @SerializedName("revision")
    val revision: Int,
)