package com.aenadgrleey.tobedone

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.work.Configuration
import androidx.work.WorkerFactory
import com.aenadgrleey.auth.ui.di.AuthUiComponent
import com.aenadgrleey.auth.ui.di.AuthUiComponentProvider
import com.aenadgrleey.list.ui.di.TodoListUiComponent
import com.aenadgrleey.list.ui.di.TodoListUiComponentProvider
import com.aenadgrleey.settings.ui.di.SettingUiComponent
import com.aenadgrleey.settings.ui.di.SettingUiComponentProvider
import com.aenadgrleey.tobedone.di.AppComponent
import com.aenadgrleey.tobedone.di.DaggerAppComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiComponentProvider
import com.aenadgrleey.todonotify.domain.TodoNotificationNavigator
import com.aenadgrleey.todonotify.ui.di.TodoNotificationActionReceiverComponent
import com.aenadgrleey.todonotify.ui.di.TodoNotificationActionReceiverComponentProvider
import com.aenadgrleey.todonotify.ui.di.TodoNotificatorComponent
import com.aenadgrleey.todonotify.ui.di.TodoNotificatorComponentProvider
import com.aenadgrleey.todonotify.ui.utils.TodoNotification
import com.google.android.material.color.DynamicColors
import javax.inject.Inject


class ToBeDone : Application(), Configuration.Provider,
    AuthUiComponentProvider,
    SettingUiComponentProvider,
    TodoListUiComponentProvider,
    TodoRefactorUiComponentProvider,
    TodoNotificatorComponentProvider,
    TodoNotificationActionReceiverComponentProvider {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerFactory: WorkerFactory

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        appComponent = DaggerAppComponent.builder()
            .applicationContext(this)
            .build()
        appComponent.injectIntoApplication(this)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun provideAuthComponentProvider(): AuthUiComponent {
        return appComponent.authUiComponent().create()
    }

    override fun provideTodoListUiComponent(): TodoListUiComponent {
        return appComponent.todoListUiComponent().create()
    }

    override fun provideTodoRefactorUiComponent(): TodoRefactorUiComponent {
        return appComponent.todoRefactorUiComponent().create()
    }

    override fun provideSettingsUiComponentProvider(): SettingUiComponent {
        return appComponent.settingsUiComponent().create()
    }

    override fun provideNotificatorComponent(): TodoNotificatorComponent =
        appComponent.todoNotificatorComponent().create(
            object : TodoNotificationNavigator {
                override val intentFromNotificationToActivity: Intent
                    get() = Intent(applicationContext, TodoActivity::class.java).apply {
                        action = TodoNotification.launchAppFromNotificationAction
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }

            }
        )

    override fun provideTodoNotificationActionReceiverComponent(): TodoNotificationActionReceiverComponent =
        appComponent.todoNotificationActionReceiverComponent().create()


}

val Context.appComponent: AppComponent
    get() = when (this) {
        is ToBeDone -> this.appComponent
        else -> this.applicationContext.appComponent
    }