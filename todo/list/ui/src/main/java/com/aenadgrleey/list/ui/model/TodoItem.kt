package com.aenadgrleey.list.ui.model

import com.aenadgrleey.todo.domain.models.Importance
import java.util.Date

/*
Model for UI layer operations
 */
data class TodoItem(
    val id: String,
    val body: String,
    val completed: Boolean,
    val importance: Importance,
    val deadline: Date? = null,
    val created: Date,
    val lastModified: Date,
)