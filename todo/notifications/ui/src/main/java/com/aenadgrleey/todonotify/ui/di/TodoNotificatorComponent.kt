package com.aenadgrleey.todonotify.ui.di

import com.aenadgrleey.todonotify.domain.TodoNotificationNavigator
import com.aenadgrleey.todonotify.ui.BootAlarmSetter
import com.aenadgrleey.todonotify.ui.notification.notificator.TodoNotificator
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface TodoNotificatorComponent {

    fun inject(todoNotificator: TodoNotificator)

    fun inject(bootAlarmSetter: BootAlarmSetter)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navigator: TodoNotificationNavigator): TodoNotificatorComponent
    }
}

interface TodoNotificatorComponentProvider {
    fun provideNotificatorComponent(): TodoNotificatorComponent
}