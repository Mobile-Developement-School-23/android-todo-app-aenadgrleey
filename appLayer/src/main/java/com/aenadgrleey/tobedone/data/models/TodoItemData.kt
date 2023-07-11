package com.aenadgrleey.tobedone.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aenadgrleey.tobedone.utils.Importance
import java.util.Calendar
import java.util.Date
import java.util.UUID

@Entity(tableName = "todo_items")
data class TodoItemData(
    @PrimaryKey
    var id: String = UUID.randomUUID().toString(),
    var body: String? = null,
    var completed: Boolean? = false,
    var importance: Importance? = Importance.Common,
    var deadline: Date? = null,
    var created: Date? = Calendar.getInstance().time,
    var lastModified: Date? = Calendar.getInstance().time,
    var color: String? = null,
    var lastModifiedBy: String? = UUID.randomUUID().toString(),
) {
    fun copy(
        body: String? = this.body,
        completed: Boolean? = this.completed,
        importance: Importance? = this.importance,
        deadline: Date? = this.deadline,
        created: Date? = this.created,
        lastModified: Date? = this.lastModified,
        color: String? = this.color,
        lastModifiedBy: String? = this.lastModifiedBy,
    ): TodoItemData =
        TodoItemData(
            this.id,
            body,
            completed,
            importance,
            deadline,
            created,
            lastModified,
            color,
            lastModifiedBy,
        )
}