package com.aenadgrleey.tobedone.presentation

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
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
import com.aenadgrleey.tobedone.presentation.catalogue_fragment.CatalogueFragment
import com.aenadgrleey.tobedone.utils.SharedPreferencesTags
import com.aenadgrleey.tobedone.work.SyncWorker
import com.aenadgrleey.tobedone.work.UpdateRemoteWorker
import com.aenadgrleey.tobedone.work.WorkersTags
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FragmentContainerView(this).apply {
            id = androidx.fragment.R.id.fragment_container_view_tag
        })
        if (supportFragmentManager.backStackEntryCount == 0)
            supportFragmentManager.commit {
                add<CatalogueFragment>(
                    androidx.fragment.R.id.fragment_container_view_tag,
                    "tag"
                )
                setReorderingAllowed(true)
            }

    }

    override fun onStop() {
        super.onStop()
        val internetConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeUpdateRemoteWorker =
            OneTimeWorkRequestBuilder<UpdateRemoteWorker>()
                .setConstraints(internetConstraint)
                .build()

        val periodicSyncWorker =
            PeriodicWorkRequestBuilder<SyncWorker>(
                20, TimeUnit.SECONDS,
                2, TimeUnit.SECONDS
            )
                .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
                .setConstraints(internetConstraint)
                .build()

        WorkManager.getInstance(applicationContext).run {
            this.enqueueUniquePeriodicWork(
                WorkersTags.PERIODIC_SYNC_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicSyncWorker
            )
            this.beginUniqueWork(
                WorkersTags.UPDATE_REMOTE_WORKER,
                ExistingWorkPolicy.KEEP,
                oneTimeUpdateRemoteWorker
            ).enqueue()
        }
    }

    override fun onStart() {
        super.onStart()
        launchYandexLogin()
    }

    private fun launchYandexLogin() {
        if (this.applicationContext.getSharedPreferences(SharedPreferencesTags.Tag, MODE_PRIVATE).getString(SharedPreferencesTags.Token, null) == null) {
            val yandexAuthSdk = YandexAuthSdk(this, YandexAuthOptions(this))
            val loginBuilder = YandexAuthLoginOptions.Builder()
            val intent = yandexAuthSdk.createLoginIntent(loginBuilder.build())
            val activityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val yandexAuthToken = yandexAuthSdk.extractToken(it.resultCode, it.data)
                    if (yandexAuthToken != null)
                        saveYandexAuthToken(yandexAuthToken.value)
                }
            }
            try {
                activityForResult.launch(intent)
            } catch (e: YandexAuthException) {
                println("some strange behavior")
            }
        }
    }

    private fun saveYandexAuthToken(token: String?) {
        this.applicationContext.getSharedPreferences(SharedPreferencesTags.Tag, MODE_PRIVATE).edit().putString(SharedPreferencesTags.Token, token).commit()
    }

}