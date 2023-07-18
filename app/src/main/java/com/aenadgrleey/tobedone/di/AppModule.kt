package com.aenadgrleey.tobedone.di

import com.aenadgrleey.auth.data.di.AuthModule
import com.aenadgrleey.settings.data.di.SettingsModule
import com.aenadgrleey.tobedone.di.view_component.TodoActivityModule
import com.aenadgrleey.todo.data.di.TodoDataModule
import com.aenadgrleey.todo.work.di.WorkModule
import com.aenadgrleey.todonotify.ui.di.TodoNotificationComponent
import dagger.Module

@Module(
    includes = [
        AuthModule::class,
        TodoDataModule::class,
        TodoActivityModule::class,
        WorkModule::class,
        SettingsModule::class,
        TodoNotificationComponent::class
    ]
)
abstract class AppModule