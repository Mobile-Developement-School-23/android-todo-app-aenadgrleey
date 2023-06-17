package com.aenadgrleey.tobedone.data

import com.aenadgrleey.tobedone.utils.Importance
import java.util.Calendar
import java.util.Date

data class TodoItemData(
    var id: String? = null,
    var body: String,
    var completed: Boolean = false,
    var importance: String = Importance.Common,
    var deadline: Date? = null,
    val created: Date = Calendar.getInstance().time,
    var lastModified: Date = Calendar.getInstance().time
) {
    fun copy(
        body: String = this.body,
        completed: Boolean = this.completed,
        importance: String = this.importance,
        deadline: Date? = this.deadline,
        created: Date = this.created,
        lastModified: Date = this.lastModified
    ): TodoItemData =
        TodoItemData(this.id, body, completed, importance, deadline, created, lastModified)
}