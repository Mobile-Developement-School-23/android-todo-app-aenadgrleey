package com.aenadgrleey.tobedone

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.aenadgrleey.tobedone.di.ApplicationComponent
import com.aenadgrleey.tobedone.di.DaggerApplicationComponent
import com.aenadgrleey.todonotify.ui.di.NotificatorComponent
import com.aenadgrleey.todonotify.ui.di.NotificatorComponentProvider
import com.google.android.material.color.DynamicColors
import javax.inject.Inject


class ToBeDone : Application(), Configuration.Provider, NotificatorComponentProvider {

    lateinit var applicationComponent: ApplicationComponent

    @Inject
    lateinit var workerFactory: WorkerFactory

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        applicationComponent = DaggerApplicationComponent.builder().applicationContext(this).build()
        applicationComponent.injectIntoApplication(this)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun provideNotificatorComponent(): NotificatorComponent =
        applicationComponent.todoNotificatorComponent().create()


}

val Context.applicationComponent: ApplicationComponent
    get() = when (this) {
        is ToBeDone -> this.applicationComponent
        else -> this.applicationContext.applicationComponent
    }