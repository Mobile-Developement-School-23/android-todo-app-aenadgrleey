package com.aenadgrleey.tobedone

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.R
import androidx.fragment.app.FragmentContainerView
import com.aenadgrleey.auth.ui.di.AuthUiComponent
import com.aenadgrleey.auth.ui.di.AuthUiComponentProvider
import com.aenadgrleey.list.ui.di.TodoListUiComponent
import com.aenadgrleey.list.ui.di.TodoListUiComponentProvider
import com.aenadgrleey.settings.ui.di.SettingUiComponent
import com.aenadgrleey.settings.ui.di.SettingUiComponentProvider
import com.aenadgrleey.tobedone.di.view_component.TodoActivityComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiComponent
import com.aenadgrleey.todo.refactor.ui.di.TodoRefactorUiComponentProvider


class TodoActivity : AppCompatActivity(),
    AuthUiComponentProvider,
    SettingUiComponentProvider,
    TodoListUiComponentProvider,
    TodoRefactorUiComponentProvider {

    private lateinit var activityComponent: TodoActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FragmentContainerView(this).apply { id = R.id.fragment_container_view_tag })
        val launcher = this.registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        activityComponent = applicationComponent.todoActivityComponent().create(
            permissionGrantLauncher = launcher,
            activity = this,
            fragmentManager = supportFragmentManager,
            lifecycleOwner = this
        )
        activityComponent.boot()
    }

    override fun provideAuthComponentProvider(): AuthUiComponent {
        return activityComponent.authUiComponent().create()
    }

    override fun provideTodoListUiComponent(): TodoListUiComponent {
        return activityComponent.todoListUiComponent().create()
    }

    override fun provideTodoRefactorUiComponent(): TodoRefactorUiComponent {
        return activityComponent.todoRefactorUiComponent().create()
    }

    override fun provideSettingsUiComponentProvider(): SettingUiComponent {
        return activityComponent.settingsUiComponent().create()
    }

}