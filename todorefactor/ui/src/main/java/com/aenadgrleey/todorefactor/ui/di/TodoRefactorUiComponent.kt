package com.aenadgrleey.todorefactor.ui.di

import com.aenadgrleey.di.FeatureScope
import com.aenadgrleey.todorefactor.domain.TodoItemId
import com.aenadgrleey.todorefactor.domain.TodoRefactorNavigator
import com.aenadgrleey.todorefactor.ui.TodoRefactorFragment
import com.aenadgrleey.todorefactor.ui.TodoRefactorViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@FeatureScope
@Subcomponent()
interface TodoRefactorUiComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navigator: TodoRefactorNavigator, @BindsInstance @TodoItemId todoItemId: String?): TodoRefactorUiComponent
    }

    fun viewModelFactory(): TodoRefactorViewModel.ViewModelFactory

    fun inject(fragment: TodoRefactorFragment)

}

interface TodoRefactorUiComponentProvider {
    fun provideTodoRefactorUiComponent(): TodoRefactorUiComponent
}