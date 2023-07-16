package com.aenadgrleey.tobedone.di

import com.aenadgrleey.auth.data.di.AuthModule
import com.aenadgrleey.settings.data.di.SettingsModule
import com.aenadgrleey.tobedone.di.view_component.TodoActivityModule
import com.aenadgrleey.tododata.di.TodoDataModule
import com.aenadgrleey.work.di.WorkModule
import dagger.Module

@Module(
    includes = [
        AuthModule::class,
        TodoDataModule::class,
        TodoActivityModule::class,
        WorkModule::class,
        SettingsModule::class
    ]
)
abstract class AppModule