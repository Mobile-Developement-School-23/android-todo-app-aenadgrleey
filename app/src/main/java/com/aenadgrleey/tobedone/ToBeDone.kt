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
import com.aenadgrleey.tobedone.di.ApplicationComponent
import com.aenadgrleey.tobedone.di.DaggerApplicationComponent
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

    lateinit var applicationComponent: ApplicationComponent

    @Inject
    lateinit var workerFactory: WorkerFactory

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationContext(this)
            .build()
        applicationComponent.injectIntoApplication(this)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun provideAuthComponentProvider(): AuthUiComponent {
        return applicationComponent.authUiComponent().create()
    }

    override fun provideTodoListUiComponent(): TodoListUiComponent {
        return applicationComponent.todoListUiComponent().create()
    }

    override fun provideTodoRefactorUiComponent(): TodoRefactorUiComponent {
        return applicationComponent.todoRefactorUiComponent().create()
    }

    override fun provideSettingsUiComponentProvider(): SettingUiComponent {
        return applicationComponent.settingsUiComponent().create()
    }

    override fun provideNotificatorComponent(): TodoNotificatorComponent =
        applicationComponent.todoNotificatorComponent().create(
            object : TodoNotificationNavigator {
                override val intentFromNotificationToActivity: Intent
                    get() = Intent(applicationContext, TodoActivity::class.java).apply {
                        action = TodoNotification.launchAppFromNotificationAction
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }

            }
        )

    override fun provideTodoNotificationActionReceiverComponent(): TodoNotificationActionReceiverComponent =
        applicationComponent.todoNotificationActionReceiverComponent().create()


}

val Context.applicationComponent: ApplicationComponent
    get() = when (this) {
        is ToBeDone -> this.applicationComponent
        else -> this.applicationContext.applicationComponent
    }