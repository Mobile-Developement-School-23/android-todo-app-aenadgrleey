package com.aenadgrleey.todonotify.ui.di

import com.aenadgrleey.todonotify.ui.notify.Notificator
import dagger.Subcomponent

@Subcomponent
interface NotificatorComponent {

    fun inject(notificator: Notificator)

    @Subcomponent.Factory
    interface Factory {
        fun create(): NotificatorComponent
    }
}

interface NotificatorComponentProvider {
    fun provideNotificatorComponent(): NotificatorComponent
}