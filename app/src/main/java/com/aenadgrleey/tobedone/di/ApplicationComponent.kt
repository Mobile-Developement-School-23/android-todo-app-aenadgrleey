package com.aenadgrleey.tobedone.di

import android.content.Context
import com.aenadgrleey.auth.data.di.AuthProviderScope
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.core.di.AppScope
import com.aenadgrleey.settings.data.di.SettingsProviderScope
import com.aenadgrleey.tobedone.ToBeDone
import com.aenadgrleey.tobedone.TodoActivity
import com.aenadgrleey.tobedone.di.broadcast.BroadcastReceiverModule
import com.aenadgrleey.tobedone.di.view_component.TodoActivityComponent
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@AppScope
@SettingsProviderScope
@AuthProviderScope
@Component(modules = [AppModule::class, BroadcastReceiverModule::class, AndroidInjectionModule::class])
interface ApplicationComponent : AndroidInjector<ToBeDone> {

    fun injectIntoApplication(application: ToBeDone)

    fun injectIntoActivity(activity: TodoActivity)

    fun todoActivityComponent(): TodoActivityComponent.Factory


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@AppContext context: Context): Builder

        fun build(): ApplicationComponent
    }

}