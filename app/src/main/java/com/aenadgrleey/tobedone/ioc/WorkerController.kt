package com.aenadgrleey.tobedone.ioc

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.todo.work.SyncWorker
import com.aenadgrleey.todo.work.UpdateRemoteWorker
import com.aenadgrleey.todo.work.WorkersTags
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkerController @Inject constructor(
    @AppContext private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
) {
    fun setUpWorkers() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                WorkManager.getInstance(context).run {
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
