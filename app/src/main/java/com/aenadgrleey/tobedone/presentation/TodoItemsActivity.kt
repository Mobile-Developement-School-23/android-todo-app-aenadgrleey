package com.aenadgrleey.tobedone.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.aenadgrleey.auth.ui.AuthFragment
import com.aenadgrleey.tobedone.applicationComponent
import com.aenadgrleey.tobedone.work.SyncWorker
import com.aenadgrleey.tobedone.work.UpdateRemoteWorker
import com.aenadgrleey.tobedone.work.WorkersTags
import java.util.concurrent.TimeUnit


class TodoItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        applicationComponent.injectIntoActivity(this)

        setContentView(FragmentContainerView(this).apply {
            id = androidx.fragment.R.id.fragment_container_view_tag
        })
        if (supportFragmentManager.backStackEntryCount == 0)
            supportFragmentManager.commit {
                add<AuthFragment>(
                    androidx.fragment.R.id.fragment_container_view_tag,
                    "tag"
                )
                setReorderingAllowed(true)
            }

    }

    override fun onStop() {
        super.onStop()

        WorkManager.getInstance(applicationContext).run {
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