package com.aenadgrleey.remote.network.responses

import com.google.gson.annotations.SerializedName

internal class TodoItemResponse(
    @SerializedName("element")
    val item: com.aenadgrleey.data.models.TodoItemData,
    @SerializedName("revision")
    val revision: Int,
)