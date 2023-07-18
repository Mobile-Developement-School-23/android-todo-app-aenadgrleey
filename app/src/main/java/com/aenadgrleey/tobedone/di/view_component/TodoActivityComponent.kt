package com.aenadgrleey.tobedone.di.view_component

import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.aenadgrleey.auth.ui.di.AuthUiComponent
import com.aenadgrleey.auth.ui.di.AuthUiModule
import com.aenadgrleey.list.ui.di.TodoListUiComponent
import com.aenadgrleey.list.ui.di.TodoListUiModule
import com.aenadgrleey.settings.ui.di.SettingUiComponent
import com.aenadgrleey.settings.ui.di.SettingsUiModule
import com.aenadgrleey.tobedone.TodoActivity
import com.aenadgrleey.tobedone.ioc.TodoActivityBootstrapper
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiModule
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        NavigationModule::class,
        AuthUiModule::class,
        TodoListUiModule::class,
        TodoRefactorUiModule::class,
        SettingsUiModule::class
    ]
)
interface TodoActivityComponent {
    fun boot(): TodoActivityBootstrapper

    fun inject(activity: TodoActivity)

    fun authUiComponent(): AuthUiComponent.Factory

    fun todoListUiComponent(): TodoListUiComponent.Factory

    fun todoRefactorUiComponent(): TodoRefactorUiComponent.Factory

    fun settingsUiComponent(): SettingUiComponent.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance permissionGrantLauncher: ActivityResultLauncher<String>,
            @BindsInstance activity: AppCompatActivity,
            @BindsInstance fragmentManager: FragmentManager,
            @BindsInstance lifecycleOwner: LifecycleOwner,
        ): TodoActivityComponent
    }
}