package com.aenadgrleey.todo.work.di

import android.net.ConnectivityManager
import android.net.Network
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerFactory
import com.aenadgrleey.core.domain.exceptions.ServerErrorException
import com.aenadgrleey.core.domain.exceptions.WrongAuthorizationException
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import com.aenadgrleey.todo.work.ChildWorkerFactory
import com.aenadgrleey.todo.work.NotificationWorker
import com.aenadgrleey.todo.work.SampleWorkerFactory
import com.aenadgrleey.todo.work.SyncWorker
import com.aenadgrleey.todo.work.UpdateRemoteWorker
import com.aenadgrleey.todo.work.WorkersConstants
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

@Module
abstract class WorkModule {
    @Binds
    @IntoMap
    @WorkerKey(UpdateRemoteWorker::class)
    abstract fun bindUpdateRemoteWorker(factory: UpdateRemoteWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SyncWorker::class)
    abstract fun bindSyncWorker(factory: SyncWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(NotificationWorker::class)
    abstract fun bindsNotificationWorker(factory: NotificationWorker.Factory): ChildWorkerFactory

    @Binds
    abstract fun bindWorkerFactory(factory: SampleWorkerFactory): WorkerFactory

    companion object {
        @Provides
        @SyncWorkerRequest
        fun providesSyncWorkerRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(
                WorkersConstants.syncWorkerRepeatPeriod.interval,
                WorkersConstants.syncWorkerRepeatPeriod.timeUnit,
                WorkersConstants.syncWorkerFlexInterval.interval,
                WorkersConstants.syncWorkerFlexInterval.timeUnit
            )
                .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
                .setConstraints(WorkersConstants.networkConstraint)
                .build()

        @Provides
        @UpdateWorkerRequest
        fun providesUpdateWorkerRequest(): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UpdateRemoteWorker>()
                .setConstraints(WorkersConstants.networkConstraint)
                .build()

        @Provides
        @NotificationWorkerRequest
        fun providesNotificationWorkerRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(
                WorkersConstants.notificationWorkerRepeatPeriod.interval,
                WorkersConstants.notificationWorkerRepeatPeriod.timeUnit,
                WorkersConstants.notificationWorkerFlexInterval.interval,
                WorkersConstants.notificationWorkerFlexInterval.timeUnit
            )
                .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
                .build()

        @Provides
        @NetworkTracker
        fun providesNetworkTracker(repository: TodoItemRepository) =
            object : ConnectivityManager.NetworkCallback() {
                // network is available for use
                override fun onAvailable(network: Network) {
                    CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                        val NETWORK_TAG = "NetworkLog"
                        try {
                            repository.fetchRemoteData()
                        } catch (unknownHostException: java.net.UnknownHostException) {
                            Log.e(NETWORK_TAG, unknownHostException.toString())
                        } catch (authErrorException: WrongAuthorizationException) {
                            Log.e(NETWORK_TAG, authErrorException.toString())
                        } catch (serverErrorException: ServerErrorException) {
                            Log.e(NETWORK_TAG, serverErrorException.toString())
                        } catch (socketTimeoutException: SocketTimeoutException) {
                            Log.e(NETWORK_TAG, socketTimeoutException.toString())
                        }
                    }
                    super.onAvailable(network)
                }
            }
    }
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)