package com.aenadgrleey.todorefactor.ui.model

sealed class UiEvent {
    object ExitRequest : UiEvent()
}