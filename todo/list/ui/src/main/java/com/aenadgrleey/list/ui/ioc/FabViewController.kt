package com.aenadgrleey.list.ui.ioc

import com.aenadgrleey.core.di.ViewScope
import com.aenadrgleey.todo.list.domain.TodoListNavigator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

@ViewScope
class FabViewController @Inject constructor(
    private val fab: FloatingActionButton,
    private val navigator: TodoListNavigator,
) {
    fun setUpFab() {
        fab.setOnClickListener { navigator.navigateToRefactorFragment(null) }
    }
}