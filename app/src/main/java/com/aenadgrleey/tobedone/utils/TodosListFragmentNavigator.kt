package com.aenadgrleey.tobedone.utils

import com.aenadgrleey.tobedone.presentation.models.TodoItem

interface TodosListFragmentNavigator {
    fun navigateToRefactorFragment(todoItem: TodoItem?): Unit

}