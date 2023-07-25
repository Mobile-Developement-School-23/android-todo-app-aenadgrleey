package com.aenadgrleey.tobedone.di

import com.aenadgrleey.auth.data.di.AuthModule
import com.aenadgrleey.auth.ui.di.AuthUiModule
import com.aenadgrleey.list.ui.di.TodoListUiModule
import com.aenadgrleey.settings.data.di.SettingsModule
import com.aenadgrleey.settings.ui.di.SettingsUiModule
import com.aenadgrleey.tobedone.di.view_component.TodoActivityViewModule
import com.aenadgrleey.todo.data.di.TodoDataModule
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiModule
import com.aenadgrleey.todo.work.di.WorkModule
import com.aenadgrleey.todonotify.ui.di.TodoNotificationModule
import dagger.Module

@Module(
    includes = [
        AuthModule::class,
        TodoDataModule::class,
        WorkModule::class,
        SettingsModule::class,
        TodoNotificationModule::class,
        //Ui:
        TodoActivityViewModule::class,
        AuthUiModule::class,
        TodoListUiModule::class,
        TodoRefactorUiModule::class,
        SettingsUiModule::class
    ]
)
abstract class AppModule