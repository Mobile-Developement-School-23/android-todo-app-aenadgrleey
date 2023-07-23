package com.aenadgrleey.todo.domain.models

import java.util.Date

data class TodoItemData(
    var id: String?,
    var body: String,
    var completed: Boolean,
    var importance: Importance,
    var deadline: Date?,
    var created: Date?,
    var lastModified: Date?,
    var color: String? = null,
    var lastModifiedBy: String?,
) {
    fun copy(
        body: String = this.body,
        completed: Boolean = this.completed,
        importance: Importance = this.importance,
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