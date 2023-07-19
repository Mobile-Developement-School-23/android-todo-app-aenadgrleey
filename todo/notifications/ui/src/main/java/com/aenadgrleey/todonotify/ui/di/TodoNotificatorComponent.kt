package com.aenadgrleey.todonotify.ui.di

import com.aenadgrleey.todonotify.ui.notification.notificator.TodoNotificator
import dagger.Subcomponent

@Subcomponent
interface TodoNotificatorComponent {

    fun inject(todoNotificator: TodoNotificator)

    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoNotificatorComponent
    }
}

interface TodoNotificatorComponentProvider {
    fun provideNotificatorComponent(): TodoNotificatorComponent
}