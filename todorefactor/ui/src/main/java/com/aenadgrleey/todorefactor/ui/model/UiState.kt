package com.aenadgrleey.todorefactor.ui.model

import com.aenadgrleey.core.domain.Importance
import java.util.Date

data class UiState(
    var text: String? = null,
    var textError: Boolean = false,
    val completeness: Boolean = false,
    var importance: Importance = Importance.Common,
    val deadlineEnabled: Boolean = false,
    var deadline: Date? = null,
    var color: String? = null,
    val id: String?,
    val lastModified: Date?,
    val created: Date?,
    val lastModifiedBy: String?,
) {
    fun copy(
        body: String? = this.text,
        completeness: Boolean = this.completeness,
        importance: Importance = this.importance,
        deadlineEnabled: Boolean = this.deadlineEnabled,
        deadline: Date? = this.deadline,
        color: String? = this.color,
    ): UiState = UiState(
        text = body,
        completeness = completeness,
        importance = importance,
        deadlineEnabled = deadlineEnabled,
        deadline = deadline,
        color = color,
        id = this.id,
        lastModified = this.lastModified,
        created = this.created,
        lastModifiedBy = this.lastModifiedBy
    )
}