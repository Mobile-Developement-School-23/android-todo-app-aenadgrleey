package com.aenadgrleey.tobedone.ioc

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.todo.work.WorkersConstants
import com.aenadgrleey.todo.work.di.NotificationWorkerRequest
import com.aenadgrleey.todo.work.di.SyncWorkerRequest
import com.aenadgrleey.todo.work.di.UpdateWorkerRequest
import javax.inject.Inject

class WorkerController @Inject constructor(
    @AppContext private val context: Context,
    @SyncWorkerRequest private val syncWorkRequest: PeriodicWorkRequest,
    @UpdateWorkerRequest private val updateWorkerRequest: OneTimeWorkRequest,
    @NotificationWorkerRequest private val notificationWorkerRequest: PeriodicWorkRequest,
    private val lifecycleOwner: LifecycleOwner,
) {
    fun setUpWorkers() {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_STOP)
                    WorkManager.getInstance(context).run {
                        this.enqueueUniquePeriodicWork(
                            WorkersConstants.PERIODIC_SYNC_WORKER,
                            ExistingPeriodicWorkPolicy.UPDATE,
                            syncWorkRequest
                        )
                        this.beginUniqueWork(
                            WorkersConstants.UPDATE_REMOTE_WORKER,
                            ExistingWorkPolicy.REPLACE,
                            updateWorkerRequest
                        ).enqueue()
                        this.enqueueUniquePeriodicWork(
                            WorkersConstants.NOTIFICATION_WORKER,
                            ExistingPeriodicWorkPolicy.UPDATE,
                            notificationWorkerRequest
                        )
                    }
            }
        })
    }
}
