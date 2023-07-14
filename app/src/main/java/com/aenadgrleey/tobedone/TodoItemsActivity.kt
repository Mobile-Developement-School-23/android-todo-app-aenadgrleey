package com.aenadgrleey.tobedone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.aenadgrleey.auth.ui.AuthNavigator
import com.aenadgrleey.auth.ui.di.AuthUiComponentProvider
import com.aenadgrleey.todolist.domain.TodoListNavigator
import com.aenadgrleey.todolist.ui.TodosListFragment
import com.aenadgrleey.todolist.ui.di.TodoListUiComponent
import com.aenadgrleey.todolist.ui.di.TodoListUiComponentProvider
import androidx.fragment.R as FragmentR


class TodoItemsActivity : AppCompatActivity(),
    AuthUiComponentProvider,
    TodoListUiComponentProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applicationComponent.injectIntoActivity(this)

        setContentView(FragmentContainerView(this).apply {
            id = androidx.fragment.R.id.fragment_container_view_tag
        })
        if (supportFragmentManager.fragments.isEmpty())
            supportFragmentManager.commit {
                add<TodosListFragment>(FragmentR.id.fragment_container_view_tag, "tag")
                setReorderingAllowed(true)
            }

    }

    override fun provideAuthComponentProvider() =
        applicationComponent.authUiComponent().create(object : AuthNavigator {
            override fun onSuccessAuth() {
                supportFragmentManager.commit {
                    add<TodosListFragment>(FragmentR.id.fragment_container_view_tag, "tag")
                    addToBackStack("")
                    setReorderingAllowed(true)
                }
            }
        })

    override fun provideTodoListUiComponent(): TodoListUiComponent =
        applicationComponent.todoListUiComponent().create(object : TodoListNavigator {
            override fun navigateToRefactorFragment(todoItemId: String?) {
//                supportFragmentManager.commit {
//                    val arguments = Bundle()
//                    arguments.putString("todoItem", todoItemId)
//                    val aim = TodoRefactorFragment().also { it.arguments = arguments }
//                    replace(
//                        androidx.fragment.R.id.fragment_container_view_tag,
//                        aim,
//                        "tag"
//                    )
//                    setReorderingAllowed(true)
//                    addToBackStack(null)
//                }
            }

        })


//    private fun createSyncWorkerRequest() =
//        PeriodicWorkRequestBuilder<com.aenadgrleey.work.SyncWorker>(
//            20, TimeUnit.SECONDS,
//            2, TimeUnit.SECONDS
//        )
//            .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
//            .setConstraints(
//                Constraints.Builder()
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .build()
//            )
//            .build()
//
//    private fun createRemoteUpdaterWorkerRequest() = OneTimeWorkRequestBuilder<com.aenadgrleey.work.UpdateRemoteWorker>()
//        .setConstraints(
//            Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build()
//        )
//        .build()
//
}