package com.aenadrgleey.todo.list.domain

interface TodoListNavigator {
    fun navigateToSettings()
    fun navigateToRefactorFragment(todoItemId: String?)

}