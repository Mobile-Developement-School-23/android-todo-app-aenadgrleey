package com.aenadgrleey.settings.ui.model

import com.aenadgrleey.settings.domain.model.AppTheme

sealed class UiAction {
    data class OnThemeSelect(val theme: AppTheme) : UiAction()
    object OnSignOutButtonClick : UiAction()
}
