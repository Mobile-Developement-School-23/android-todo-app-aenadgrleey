package com.aenadgrleey.tobedone

import android.app.Application
import android.content.Context
import com.aenadgrleey.tobedone.di.ApplicationComponent
import com.aenadgrleey.tobedone.di.DaggerApplicationComponent
import com.google.android.material.color.DynamicColors


class ToBeDone : Application()
//    Configuration.Provider
{

    lateinit var applicationComponent: ApplicationComponent

//    @Inject
//    lateinit var workerFactory: WorkerFactory

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        applicationComponent = DaggerApplicationComponent.builder().applicationContext(this).build()
        applicationComponent.injectIntoApplication(this)
    }

//    override fun getWorkManagerConfiguration(): Configuration =
//        Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
}

val Context.applicationComponent: ApplicationComponent
    get() = when (this) {
        is ToBeDone -> this.applicationComponent
        else -> this.applicationContext.applicationComponent
    }