package com.aenadgrleey.tobedone.di

import com.aenadgrleey.auth.data.di.AuthModule
import com.aenadgrleey.tobedone.di.view_component.TodoActivityModule
import com.aenadgrleey.tododata.di.TodoDataModule
import dagger.Module

@Module(
    includes = [
        AuthModule::class,
        TodoDataModule::class,
        TodoActivityModule::class
//    WorkModule::class
    ]
)
abstract class AppModule