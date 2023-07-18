package com.aenadgrleey.todo.work.di

import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import com.aenadgrleey.todo.work.ChildWorkerFactory
import com.aenadgrleey.todo.work.SampleWorkerFactory
import com.aenadgrleey.todo.work.SyncWorker
import com.aenadgrleey.todo.work.UpdateRemoteWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module()
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

}

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)