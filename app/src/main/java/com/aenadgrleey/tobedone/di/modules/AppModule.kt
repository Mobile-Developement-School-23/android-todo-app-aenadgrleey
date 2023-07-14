package com.aenadgrleey.tobedone.di.modules

import com.aenadgrleey.auth.data.di.AuthModule
import com.aenadgrleey.auth.ui.di.AuthUiModule
import com.aenadgrleey.tododata.di.DataModule
import com.aenadgrleey.todolist.ui.di.TodoListUiModule
import dagger.Module

@Module(
    includes = [DataModule::class,
//    WorkModule::class
        AuthModule::class, AuthUiModule::class,
        TodoListUiModule::class
    ]
)
abstract class AppModule