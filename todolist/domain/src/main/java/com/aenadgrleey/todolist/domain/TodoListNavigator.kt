package com.aenadgrleey.todolist.domain

interface TodoListNavigator {
    fun navigateToSettings()
    fun navigateToRefactorFragment(todoItemId: String?)

}