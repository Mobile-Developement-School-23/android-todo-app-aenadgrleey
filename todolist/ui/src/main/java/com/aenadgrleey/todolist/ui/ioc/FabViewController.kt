package com.aenadgrleey.todolist.ui.ioc

import com.aenadgrleey.di.ViewScope
import com.aenadgrleey.todolist.domain.TodoListNavigator
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