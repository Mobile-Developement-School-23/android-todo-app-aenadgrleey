package com.aenadgrleey.tobedone.di

import android.content.Context
import com.aenadgrleey.auth.data.di.AuthProviderScope
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.core.di.AppScope
import com.aenadgrleey.settings.data.di.SettingsProviderScope
import com.aenadgrleey.tobedone.ToBeDone
import com.aenadgrleey.tobedone.TodoActivity
import com.aenadgrleey.tobedone.di.view_component.TodoActivityComponent
import com.aenadgrleey.todonotify.domain.TodoNotificationNavigator
import com.aenadgrleey.todonotify.ui.di.TodoNotificationActionReceiverComponent
import com.aenadgrleey.todonotify.ui.di.TodoNotificatorComponent
import dagger.BindsInstance
import dagger.Component

@AppScope
@SettingsProviderScope
@AuthProviderScope
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun injectIntoApplication(application: ToBeDone)

    fun injectIntoActivity(activity: TodoActivity)

    fun todoActivityComponent(): TodoActivityComponent.Factory

    fun todoNotificatorComponent(): TodoNotificatorComponent.Factory

    fun todoNotificationActionReceiverComponent(): TodoNotificationActionReceiverComponent.Factory


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@AppContext context: Context): Builder

        @BindsInstance
        fun notificationNavigator(navigator: TodoNotificationNavigator): Builder

        fun build(): ApplicationComponent
    }

}