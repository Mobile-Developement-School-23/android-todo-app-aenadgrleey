package com.aenadgrleey.todolist.ui.model

sealed class UiEvent {

    sealed class RecyclerEvent {
        object ScrollUp : RecyclerEvent()
    }

    sealed class CoordinatorEvent {
        class DeleteItem(todoItem: com.aenadgrleey.ui.TodoItem) : CoordinatorEvent()
    }

    object ToggleCompletedMark : UiEvent()
    object SyncedWithServer : UiEvent()
    object SyncingWithServer : UiEvent()
    object ConnectionError : UiEvent()
    object BadServerResponse : UiEvent()

}
