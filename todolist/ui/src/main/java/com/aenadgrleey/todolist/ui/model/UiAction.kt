package com.aenadgrleey.todolist.ui.model

sealed class UiAction {
    object ScrollUpRequest : UiAction()
    object ToggledCompletedMark : UiAction()
    object RefreshTodoItems : UiAction()
    data class AddTodoItem(val todoItem: com.aenadgrleey.ui.TodoItem) : UiAction()
    data class DeleteTodoItem(val todoItem: com.aenadgrleey.ui.TodoItem) : UiAction()
}