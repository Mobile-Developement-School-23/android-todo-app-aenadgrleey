package com.aenadgrleey.todonotify.ui.di

import com.aenadgrleey.todonotify.ui.notification.notificator.TodoNotificationActionReceiver
import dagger.Subcomponent

@Subcomponent
interface TodoNotificationActionReceiverComponent {
    fun inject(actionReceiver: TodoNotificationActionReceiver)

    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoNotificationActionReceiverComponent
    }
}

interface TodoNotificationActionReceiverComponentProvider {
    fun provideTodoNotificationActionReceiverComponent(): TodoNotificationActionReceiverComponent
}