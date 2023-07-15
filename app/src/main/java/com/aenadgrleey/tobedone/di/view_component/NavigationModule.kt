package com.aenadgrleey.tobedone.di.view_component

import com.aenadgrleey.auth.domain.AuthNavigator
import com.aenadgrleey.tobedone.ioc.NavigationController
import com.aenadgrleey.todolist.domain.TodoListNavigator
import com.aenadgrleey.todorefactor.domain.TodoRefactorNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigationModule {
    @ActivityScope
    @Binds
    abstract fun bindsAuthNavigator(controller: NavigationController): AuthNavigator

    @ActivityScope
    @Binds
    abstract fun bindsTodoListNavigator(controller: NavigationController): TodoListNavigator

    @ActivityScope
    @Binds
    abstract fun bindsTodoRefactorNavigator(controller: NavigationController): TodoRefactorNavigator

}
