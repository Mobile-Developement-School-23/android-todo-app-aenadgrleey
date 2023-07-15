package com.aenadgrleey.todorefactor.ui.model

import com.aenadgrleey.core.domain.Importance
import java.util.Date

sealed class UiAction {
    data class OnTextChange(val text: String) : UiAction()

    data class OnImportanceChange(val importance: Importance) : UiAction()

    data class OnCompletenessChange(val completeness: Boolean) : UiAction()

    data class OnDeadlineChange(val enabled: Boolean, val deadline: Date?) : UiAction()

    object OnSaveRequest : UiAction()

    object OnDeleteRequest : UiAction()

    object OnExitRequest : UiAction()
}