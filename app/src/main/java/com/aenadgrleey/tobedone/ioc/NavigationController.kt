package com.aenadgrleey.tobedone.ioc

import android.os.Bundle
import androidx.fragment.R
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
import com.aenadgrleey.tobedone.di.view_component.ActivityScope
import com.aenadgrleey.todolist.domain.TodoListNavigator
import com.aenadgrleey.todolist.ui.TodosListFragment
import com.aenadgrleey.todorefactor.domain.TodoItemId
import com.aenadgrleey.todorefactor.domain.TodoRefactorNavigator
import com.aenadgrleey.todorefactor.ui.TodoRefactorFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class NavigationController
@Inject constructor(
    private val authProvider: AuthProvider,
    private val supportFragmentManager: FragmentManager,
    private val lifecycleOwner: LifecycleOwner,
) : AuthNavigator, TodoListNavigator, TodoRefactorNavigator {

    val refactoredTodoItemId get() = mRefactoredTodoItemId
    private var mRefactoredTodoItemId: String? = null

    fun setUpNavigation() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authProvider.authInfoFlow().collectLatest {
                    if (it.authToken == null || it.deviceId == null)
                        supportFragmentManager.commit {
                            val aim = AuthFragment()
                            replace(R.id.fragment_container_view_tag, aim, "tag")
                            setReorderingAllowed(true)
                        }
                }
            }
        }
        if (supportFragmentManager.fragments.isEmpty())
            supportFragmentManager.commit {
                add<TodosListFragment>(R.id.fragment_container_view_tag, "tag")
                setReorderingAllowed(true)
            }
    }

    override fun onSuccessAuth() {
        supportFragmentManager.commit {
            add<TodosListFragment>(R.id.fragment_container_view_tag, "tag")
            setReorderingAllowed(true)
        }
    }

    override fun navigateToRefactorFragment(todoItemId: String?) {
        supportFragmentManager.commit {
            mRefactoredTodoItemId = todoItemId
            val argument = Bundle()
            argument.putString(TodoItemId.TAG, todoItemId)
            val aim = TodoRefactorFragment().apply { arguments = argument }
            replace(R.id.fragment_container_view_tag, aim, "tag")
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    override fun exitRefactor() {
        supportFragmentManager.popBackStack()
    }
}