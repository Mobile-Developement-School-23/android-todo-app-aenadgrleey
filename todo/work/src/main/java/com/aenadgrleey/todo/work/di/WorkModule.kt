package com.aenadgrleey.todo.work.di

import androidx.work.BackoffPolicy
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerFactory
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

    }

}

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)