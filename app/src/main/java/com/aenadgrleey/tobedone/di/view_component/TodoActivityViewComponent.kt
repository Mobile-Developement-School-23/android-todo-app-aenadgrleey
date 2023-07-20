package com.aenadgrleey.tobedone.di.view_component

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.aenadgrleey.auth.ui.di.AuthViewComponent
import com.aenadgrleey.auth.ui.di.AuthViewModule
import com.aenadgrleey.list.ui.di.TodoListViewComponent
import com.aenadgrleey.list.ui.di.TodoListViewModule
import com.aenadgrleey.settings.ui.di.SettingsViewComponent
import com.aenadgrleey.settings.ui.di.SettingsViewModule
import com.aenadgrleey.tobedone.TodoActivity
import com.aenadgrleey.tobedone.ioc.TodoActivityViewBootstrapper
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorViewComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorViewModule
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityViewScope
@Subcomponent(
    modules = [AuthViewModule::class,
        SettingsViewModule::class,
        TodoListViewModule::class,
        TodoRefactorViewModule::class,
        NavigationModule::class]
)
interface TodoActivityViewComponent {
    fun boot(): TodoActivityViewBootstrapper

    fun inject(activity: TodoActivity)

    fun authViewComponent(): AuthViewComponent.Factory

    fun settingsViewComponent(): SettingsViewComponent.Factory

    fun todoListViewComponent(): TodoListViewComponent.Factory

    fun todoRefactorViewComponent(): TodoRefactorViewComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance permissionGrantLauncher: ActivityResultLauncher<String>,
            @BindsInstance activity: AppCompatActivity,
            @BindsInstance fragmentManager: FragmentManager,
            @BindsInstance lifecycleOwner: LifecycleOwner,
        ): TodoActivityViewComponent
    }
}