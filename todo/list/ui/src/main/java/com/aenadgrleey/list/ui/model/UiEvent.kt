package com.aenadgrleey.list.ui.model

sealed class UiEvent {

    sealed class RecyclerEvent {
        object ScrollUp : RecyclerEvent()
        object ImmediateScrollUp : RecyclerEvent()
    }

    sealed class CoordinatorEvent {
        class DeleteItem(todoItem: TodoItem) : CoordinatorEvent()
    }

    object ToggleCompletedMark : UiEvent()
    object SyncedWithServer : UiEvent()
    object SyncingWithServer : UiEvent()
    object ConnectionError : UiEvent()
    object BadServerResponse : UiEvent()

}
