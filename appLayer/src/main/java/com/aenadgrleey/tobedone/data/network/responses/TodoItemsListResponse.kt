package com.aenadgrleey.tobedone.data.network.responses

import com.aenadgrleey.tobedone.data.models.TodoItemData
import com.google.gson.annotations.SerializedName

class TodoItemsListResponse(
    @SerializedName("list")
    val todoItemsList: List<TodoItemData>,
    @SerializedName("revision")
    val revision: Int
)