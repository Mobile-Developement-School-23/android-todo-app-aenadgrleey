package com.aenadgrleey.list.ui.di

import com.aenadgrleey.core.di.FeatureScope
import com.aenadgrleey.list.ui.TodoListViewModel
import dagger.Subcomponent

@FeatureScope
@Subcomponent()
interface TodoListUiComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoListUiComponent
    }

    fun viewModelFactory(): TodoListViewModel.ViewModelFactory

}

interface TodoListUiComponentProvider {
    fun provideTodoListUiComponent(): TodoListUiComponent
}