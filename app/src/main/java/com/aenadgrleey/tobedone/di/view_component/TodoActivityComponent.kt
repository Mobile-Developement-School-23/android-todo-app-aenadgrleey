package com.aenadgrleey.tobedone.di.view_component

import android.app.Activity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.aenadgrleey.auth.ui.di.AuthUiComponent
import com.aenadgrleey.auth.ui.di.AuthUiModule
import com.aenadgrleey.tobedone.ioc.TodoActivityBootstrapper
import com.aenadgrleey.todolist.ui.di.TodoListUiComponent
import com.aenadgrleey.todolist.ui.di.TodoListUiModule
import com.aenadgrleey.todorefactor.ui.di.TodoRefactorUiComponent
import com.aenadgrleey.todorefactor.ui.di.TodoRefactorUiModule
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        NavigationModule::class,
        AuthUiModule::class,
        TodoListUiModule::class,
        TodoRefactorUiModule::class,
    ]
)
interface TodoActivityComponent {
    fun boot(): TodoActivityBootstrapper

    fun inject(activity: Activity)

    fun authUiComponent(): AuthUiComponent.Factory

    fun todoListUiComponent(): TodoListUiComponent.Factory

    fun todoRefactorUiComponent(): TodoRefactorUiComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragmentManager: FragmentManager,
            @BindsInstance lifecycleOwner: LifecycleOwner,
        ): TodoActivityComponent
    }
}