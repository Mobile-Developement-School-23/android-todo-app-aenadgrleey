package com.aenadgrleey.core.domain.models

import com.aenadgrleey.core.domain.Importance
import java.util.Calendar
import java.util.Date
import java.util.UUID

data class TodoItemData(
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