package com.aenadgrleey.todolist.ui.di

import com.aenadgrleey.core.di.FeatureScope
import com.aenadgrleey.todolist.ui.TodoListViewModel
import com.aenadgrleey.todolist.ui.TodosListFragment
import com.aenadgrleey.todolist.ui.di.view_component.TodoListViewComponent
import com.aenadgrleey.todolist.ui.di.view_component.TodoListViewModule
import dagger.Subcomponent

@FeatureScope
@Subcomponent(modules = [TodoListViewModule::class])
interface TodoListUiComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TodoListUiComponent
    }

    fun viewModelFactory(): TodoListViewModel.ViewModelFactory

    fun inject(fragment: TodosListFragment)

    fun todoListViewComponent(): TodoListViewComponent.Factory
}

interface TodoListUiComponentProvider {
    fun provideTodoListUiComponent(): TodoListUiComponent
}