package com.aenadgrleey.tobedone.data.network.responses

import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.google.gson.annotations.SerializedName

class TodoItemResponse(
    @SerializedName("element")
    val item: TodoItemData,
    @SerializedName("revision")
    val revision: Int
)