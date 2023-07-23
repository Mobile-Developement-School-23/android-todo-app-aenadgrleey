package com.aenadgrleey.todo.data.remote.models

import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.google.gson.annotations.SerializedName
import java.util.Date

data class TodoItemDataNetwork(
    @SerializedName("id")
    var id: String?,
    @SerializedName("text")
    var body: String?,
    @SerializedName("done")
    var completed: Boolean?,
    @SerializedName("importance")
    var importance: Importance?,
    @SerializedName("deadline")
    var deadline: Date?,
    @SerializedName("created_at")
    var created: Date?,
    @SerializedName("changed_at")
    var lastModified: Date?,
    @SerializedName("color")
    var color: String?,
    @SerializedName("last_updated_by")
    var lastModifiedBy: String?,
) {
    fun toTodoItemData(): TodoItemData = TodoItemData(
        id!!,
        body!!,
        completed!!,
        importance!!,
        deadline,
        created!!,
        lastModified!!,
        color,
        lastModifiedBy!!,
    )
}