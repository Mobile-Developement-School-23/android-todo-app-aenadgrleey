package com.aenadgrleey.todolist.ui.model

import com.aenadgrleey.core.domain.Importance
import java.util.Date

/*
Model for UI layer operations
 */
data class TodoItem(
    val id: String? = null,
    val body: String? = null,
    val completed: Boolean? = null,
    val importance: Importance? = null,
    val deadline: Date? = null,
    val created: Date? = null,
    val lastModified: Date? = null,
) {
    fun copy(
        body: String? = this.body,
        completed: Boolean? = this.completed,
        importance: Importance? = this.importance,
        deadline: Date? = this.deadline,
        created: Date? = this.created,
        lastModified: Date? = this.lastModified,
    ): TodoItem = TodoItem(this.id, body, completed, importance, deadline, created, lastModified)
}