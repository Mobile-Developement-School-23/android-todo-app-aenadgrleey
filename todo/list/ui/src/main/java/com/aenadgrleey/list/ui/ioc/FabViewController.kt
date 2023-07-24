package com.aenadgrleey.list.ui.ioc

import android.os.Build
import android.view.HapticFeedbackConstants
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
        fab.setOnClickListener {
            navigator.navigateToRefactorFragment(null)
            it.performHapticFeedback(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
                else HapticFeedbackConstants.CONTEXT_CLICK
            )
        }
    }
}