package com.aenadgrleey.tobedone.ioc

import android.os.Bundle
import androidx.fragment.R
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aenadgrleey.auth.domain.AuthNavigator
import com.aenadgrleey.auth.domain.AuthProvider
import com.aenadgrleey.auth.ui.AuthFragment
import com.aenadgrleey.list.ui.TodosListFragment
import com.aenadgrleey.settings.domain.SettingsNavigator
import com.aenadgrleey.settings.ui.SettingDialogFragment
import com.aenadgrleey.tobedone.di.view_component.ActivityScope
import com.aenadgrleey.todo.refactor.domain.TodoItemId
import com.aenadgrleey.todo.refactor.domain.TodoRefactorNavigator
import com.aenadgrleey.todo.refactor.ui.TodoRefactorFragment
import com.aenadrgleey.todo.list.domain.TodoListNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class NavigationController
@Inject constructor(
    private val authProvider: AuthProvider,
    private val supportFragmentManager: FragmentManager,
    private val lifecycleOwner: LifecycleOwner,
) : AuthNavigator, TodoListNavigator, TodoRefactorNavigator, SettingsNavigator {

    val refactoredTodoItemId get() = mRefactoredTodoItemId
    private var mRefactoredTodoItemId: String? = null

    fun setUpNavigationControl() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authProvider.authInfoFlow().collectLatest {
                    if (it.authToken == null || it.deviceId == null)
                        supportFragmentManager.commit {
                            add<AuthFragment>(R.id.fragment_container_view_tag, "todolist")
                            setReorderingAllowed(true)
                        }
                }
            }
        }
        if (supportFragmentManager.fragments.isEmpty())
            supportFragmentManager.commit {
                add<TodosListFragment>(R.id.fragment_container_view_tag, "todolist")
                setReorderingAllowed(true)
            }
    }

    override fun onSuccessAuth() {
        supportFragmentManager.commit {
            add<TodosListFragment>(R.id.fragment_container_view_tag, "todolist")
            setReorderingAllowed(true)
        }
    }

    override fun navigateToSettings() {
        SettingDialogFragment().show(supportFragmentManager, "settings")
    }

    override fun navigateToRefactorFragment(todoItemId: String?) {
        supportFragmentManager.commit {
            mRefactoredTodoItemId = todoItemId
            val arguments = Bundle()
            arguments.putString(TodoItemId.TAG, todoItemId)
            add<TodoRefactorFragment>(R.id.fragment_container_view_tag, "todorefactor", arguments)
            setReorderingAllowed(true)
            addToBackStack("todoRefactor")

        }
    }

    override fun exitRefactor() {
        supportFragmentManager.popBackStack()
    }

    override fun exitSettings() {
        (supportFragmentManager.findFragmentByTag("settings") as DialogFragment).dismiss()
    }
}