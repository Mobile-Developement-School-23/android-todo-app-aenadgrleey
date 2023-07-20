package com.aenadgrleey.tobedone.di.view_component

import com.aenadgrleey.auth.domain.AuthNavigator
import com.aenadgrleey.settings.domain.SettingsNavigator
import com.aenadgrleey.tobedone.ioc.NavigationController
import com.aenadgrleey.todo.refactor.domain.TodoRefactorNavigator
import com.aenadrgleey.todo.list.domain.TodoListNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigationModule {
    @ActivityViewScope
    @Binds
    abstract fun bindsAuthNavigator(controller: NavigationController): AuthNavigator

    @ActivityViewScope
    @Binds
    abstract fun bindsTodoListNavigator(controller: NavigationController): TodoListNavigator

    @ActivityViewScope
    @Binds
    abstract fun bindsTodoRefactorNavigator(controller: NavigationController): TodoRefactorNavigator

    @ActivityViewScope
    @Binds
    abstract fun bindsSettingsNavigator(controller: NavigationController): SettingsNavigator

}
