package com.aenadgrleey.tobedone.di

import android.content.Context
import com.aenadgrleey.auth.data.di.AuthModule
import com.aenadgrleey.auth.data.di.AuthProviderScope
import com.aenadgrleey.auth.ui.di.AuthUiComponent
import com.aenadgrleey.auth.ui.di.AuthUiModule
import com.aenadgrleey.di.AppContext
import com.aenadgrleey.tobedone.ToBeDone
import com.aenadgrleey.tobedone.di.modules.AppModule
import com.aenadgrleey.tobedone.presentation.TodoItemsActivity
import com.aenadgrleey.tobedone.presentation.stateholders.SharedViewModel
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@AuthProviderScope
@Component(
    modules = [AppModule::class,
        AuthUiModule::class, AuthModule::class]
)
interface ApplicationComponent {

    fun injectIntoApplication(application: ToBeDone)

    fun injectIntoActivity(activity: TodoItemsActivity)

    fun viewModelsFactory(): SharedViewModel.ViewModelFactory

    fun authUiComponent(): AuthUiComponent.Factory


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@AppContext context: Context): Builder

        fun build(): ApplicationComponent
    }

}