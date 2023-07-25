package com.aenadgrleey.todo.refactor.ui.model

sealed class UiEvent {
    object ExitRequest : UiEvent()
    object TodoItemSaved : UiEvent()
}