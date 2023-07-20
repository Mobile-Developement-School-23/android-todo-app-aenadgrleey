package com.aenadgrleey.todo.refactor.ui.di

import com.aenadgrleey.core.di.FeatureScope
import com.aenadgrleey.todo.refactor.ui.TodoRefactorViewModel
import dagger.Subcomponent

@FeatureScope
@Subcomponent()
interface TodoRefactorUiComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoRefactorUiComponent
    }

    fun viewModelFactory(): TodoRefactorViewModel.ViewModelFactory

}

interface TodoRefactorUiComponentProvider {
    fun provideTodoRefactorUiComponent(): TodoRefactorUiComponent
}