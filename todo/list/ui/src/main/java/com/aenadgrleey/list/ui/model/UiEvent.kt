package com.aenadgrleey.list.ui.model

sealed class UiEvent {
    object ScrollUp : UiEvent()
    object ImmediateScrollUp : UiEvent()
    data class ShowDeletedItem(val todoItem: TodoItem) : UiEvent()
    object ToggleCompletedMark : UiEvent()
    object SyncedWithServer : UiEvent()
    object SyncingWithServer : UiEvent()
    object ConnectionError : UiEvent()
    object BadServerResponse : UiEvent()
    object ProblemsWithAuth : UiEvent()
    data class VisibilityChange(val visible: Boolean) : UiEvent()

}
