package com.aenadgrleey.tobedone

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.result.contract.ActivityResultContracts
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
import com.aenadgrleey.list.ui.di.TodoListUiComponent
import com.aenadgrleey.list.ui.di.TodoListUiComponentProvider
import com.aenadgrleey.settings.ui.di.SettingUiComponent
import com.aenadgrleey.settings.ui.di.SettingUiComponentProvider
import com.aenadgrleey.tobedone.di.view_component.TodoActivityComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiComponentProvider
import com.aenadgrleey.todo.work.SyncWorker
import com.aenadgrleey.todo.work.UpdateRemoteWorker
import com.aenadgrleey.todo.work.WorkersTags
import com.aenadgrleey.todonotify.ui.TodoNotificationDispatcherImpl
import java.util.concurrent.TimeUnit


class TodoActivity : AppCompatActivity(),
    AuthUiComponentProvider,
    SettingUiComponentProvider,
    TodoListUiComponentProvider,
    TodoRefactorUiComponentProvider {

    private lateinit var activityComponent: TodoActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FragmentContainerView(this).apply { id = R.id.fragment_container_view_tag })
        val launcher = this.registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        activityComponent = applicationComponent.todoActivityComponent().create(
            permissionGrantLauncher = launcher,
            activity = this,
            fragmentManager = supportFragmentManager,
            lifecycleOwner = this
        )
        activityComponent.boot()
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TodoNotificationDispatcherImpl::class.java).also { intent ->
            bindService(intent, object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    println("connecting service")
                }

                override fun onServiceDisconnected(name: ComponentName?) {}
            }, Context.BIND_AUTO_CREATE)
        }
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

    override fun provideSettingsUiComponentProvider(): SettingUiComponent {
        return activityComponent.settingsUiComponent().create()
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