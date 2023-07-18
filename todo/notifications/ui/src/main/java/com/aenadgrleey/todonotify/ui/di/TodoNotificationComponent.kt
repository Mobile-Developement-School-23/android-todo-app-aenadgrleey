package com.aenadgrleey.todonotify.ui.di

import com.aenadgrleey.core.di.AppScope
import com.aenadgrleey.todonotify.domain.TodoNotificationDispatcher
import com.aenadgrleey.todonotify.ui.TodoNotificationDispatcherImpl
import dagger.Binds
import dagger.Module

@Module(subcomponents = [NotificatorComponent::class])
abstract class TodoNotificationComponent {
    @Binds
    @AppScope
    abstract fun bindsNotifyDispatcher(todoDeadlineTrackerImpl: TodoNotificationDispatcherImpl): TodoNotificationDispatcher
}