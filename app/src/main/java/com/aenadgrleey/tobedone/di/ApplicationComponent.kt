package com.aenadgrleey.tobedone.di

import android.content.Context
import com.aenadgrleey.auth.data.di.AuthProviderScope
import com.aenadgrleey.auth.ui.di.AuthUiComponent
import com.aenadgrleey.di.AppContext
import com.aenadgrleey.di.AppScope
import com.aenadgrleey.tobedone.ToBeDone
import com.aenadgrleey.tobedone.TodoItemsActivity
import com.aenadgrleey.tobedone.di.modules.AppModule
import com.aenadgrleey.todolist.ui.di.TodoListUiComponent
import com.aenadgrleey.todorefactor.ui.di.TodoRefactorUiComponent
import dagger.BindsInstance
import dagger.Component

@AppScope
@AuthProviderScope
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun injectIntoApplication(application: ToBeDone)

    fun injectIntoActivity(activity: TodoItemsActivity)

    fun viewModelsFactory(): com.aenadgrleey.todolist.ui.TodoListViewModel.ViewModelFactory

    fun authUiComponent(): AuthUiComponent.Factory

    fun todoListUiComponent(): TodoListUiComponent.Factory

    fun todoRefactorUiComponent(): TodoRefactorUiComponent.Factory


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@AppContext context: Context): Builder

        fun build(): ApplicationComponent
    }

}