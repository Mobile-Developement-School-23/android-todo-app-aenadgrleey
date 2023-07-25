package com.aenadgrleey.todonotify.ui.di

import com.aenadgrleey.core.di.AppScope
import com.aenadgrleey.todonotify.domain.TodoNotificationDispatcher
import com.aenadgrleey.todonotify.ui.TodoNotificationDispatcherImpl
import dagger.Binds
import dagger.Module

@Module(subcomponents = [TodoNotificatorComponent::class])
abstract class TodoNotificationModule {
    @Binds
    @AppScope
    abstract fun bindsNotifyDispatcher(todoDeadlineTrackerImpl: TodoNotificationDispatcherImpl): TodoNotificationDispatcher
}