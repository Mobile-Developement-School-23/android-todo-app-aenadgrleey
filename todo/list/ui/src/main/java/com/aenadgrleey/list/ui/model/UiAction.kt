package com.aenadgrleey.list.ui.model

sealed class UiAction {
    object ScrollUpRequest : UiAction()
    object ToggledCompletedMark : UiAction()
    object RefreshTodoItems : UiAction()
    data class AddTodoItem(val todoItem: TodoItem) : UiAction()
    data class DeleteTodoItem(val todoItem: TodoItem) : UiAction()
}