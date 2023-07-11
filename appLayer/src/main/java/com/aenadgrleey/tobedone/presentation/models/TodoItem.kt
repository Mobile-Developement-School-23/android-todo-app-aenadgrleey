package com.aenadgrleey.tobedone.presentation.models

import android.os.Parcelable
import com.aenadgrleey.tobedone.utils.Importance
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class TodoItem(
    val id: String? = null,
    val body: String? = null,
    val completed: Boolean? = null,
    val importance: Importance? = null,
    val deadline: Date? = null,
    val created: Date? = null,
    val lastModified: Date? = null
) : Parcelable {
    fun copy(
        body: String? = this.body,
        completed: Boolean? = this.completed,
        importance: Importance? = this.importance,
        deadline: Date? = this.deadline,
        created: Date? = this.created,
        lastModified: Date? = this.lastModified
    ): TodoItem = TodoItem(this.id, body, completed, importance, deadline, created, lastModified)
}