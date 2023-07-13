package com.aenadgrleey.auth.ui.models

sealed class UiEvent {
    object AuthSuccess : UiEvent()

    object AuthRequest : UiEvent()
}