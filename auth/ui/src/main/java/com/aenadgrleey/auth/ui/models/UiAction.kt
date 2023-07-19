package com.aenadgrleey.auth.ui.models

sealed class UiAction {
    object OnSignInButtonClick : UiAction()

    data class OnSignIn(val token: String) : UiAction()
}