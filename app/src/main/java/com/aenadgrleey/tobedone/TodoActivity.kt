package com.aenadgrleey.tobedone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.R
import androidx.fragment.app.FragmentContainerView
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.aenadgrleey.auth.ui.di.AuthUiComponent
import com.aenadgrleey.auth.ui.di.AuthUiComponentProvider
import com.aenadgrleey.tobedone.di.view_component.TodoActivityComponent
import com.aenadgrleey.todolist.ui.di.TodoListUiComponent
import com.aenadgrleey.todolist.ui.di.TodoListUiComponentProvider
import com.aenadgrleey.todorefactor.ui.di.TodoRefactorUiComponent
import com.aenadgrleey.todorefactor.ui.di.TodoRefactorUiComponentProvider
import com.aenadgrleey.work.SyncWorker
import com.aenadgrleey.work.UpdateRemoteWorker
import com.aenadgrleey.work.WorkersTags
import java.util.concurrent.TimeUnit


class TodoActivity : AppCompatActivity(),
    AuthUiComponentProvider,
    TodoListUiComponentProvider,
    TodoRefactorUiComponentProvider {

    private lateinit var activityComponent: TodoActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FragmentContainerView(this).apply { id = R.id.fragment_container_view_tag })
        println("provided new fm")
        activityComponent = applicationComponent.todoActivityComponent().create(
            fragmentManager = supportFragmentManager,
            lifecycleOwner = this
        )
        activityComponent.boot()
    }

    override fun onStop() {
        super.onStop()
        setUpWorkers()
    }


    override fun provideAuthComponentProvider(): AuthUiComponent {
        return activityComponent.authUiComponent().create()
    }

    override fun provideTodoListUiComponent(): TodoListUiComponent {
        return activityComponent.todoListUiComponent().create()
    }

    override fun provideTodoRefactorUiComponent(): TodoRefactorUiComponent {
        return activityComponent.todoRefactorUiComponent().create()
    }

    fun setUpWorkers() {
        WorkManager.getInstance(this).run {
            this.enqueueUniquePeriodicWork(
                WorkersTags.PERIODIC_SYNC_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                createSyncWorkerRequest()
            )
            this.beginUniqueWork(
                WorkersTags.UPDATE_REMOTE_WORKER,
                ExistingWorkPolicy.KEEP,
                createRemoteUpdaterWorkerRequest()
            ).enqueue()
        }
    }

    private fun createSyncWorkerRequest() =
        PeriodicWorkRequestBuilder<SyncWorker>(
            20, TimeUnit.SECONDS,
            2, TimeUnit.SECONDS
        )
            .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

    private fun createRemoteUpdaterWorkerRequest() = OneTimeWorkRequestBuilder<UpdateRemoteWorker>()
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
        .build()

}