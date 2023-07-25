package com.aenadgrleey.todo.refactor.ui.model

import com.aenadgrleey.todo.domain.models.Importance
import java.util.Date

sealed class UiAction {
    data class InitTodoItem(val id: String?) : UiAction()
    object ResetTodoItem : UiAction()
    data class OnTextChange(val text: String) : UiAction()

    data class OnImportanceChange(val importance: Importance) : UiAction()

    data class OnCompletenessChange(val completeness: Boolean) : UiAction()

    data class OnDeadlineChange(val enabled: Boolean, val deadline: Date?) : UiAction()

    object OnSaveRequest : UiAction()

    object OnDeleteRequest : UiAction()

    object OnExitRequest : UiAction()
}