package com.aenadgrleey.list.ui.di

import com.aenadgrleey.list.ui.TodoListViewModel
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.list.ui.utils.TodoItemsRecyclerViewAdapter
import com.aenadrgleey.todo.list.domain.TodoListNavigator
import dagger.Module
import dagger.Provides

@Module(subcomponents = [])
object TodoListViewModule {
    @Provides
    fun providesTodoItemsAdapter(navigator: TodoListNavigator, viewModel: TodoListViewModel): TodoItemsRecyclerViewAdapter {
        return TodoItemsRecyclerViewAdapter(
            scrollUp = { viewModel.onUiAction(UiAction.ScrollUpRequest) },
            onTodoItemClick = { navigator.navigateToRefactorFragment(it.id) },
            onLastItemClick = { navigator.navigateToRefactorFragment(null) },
            onCompleteButtonClick = { viewModel.onUiAction(UiAction.AddTodoItem(it.copy(completed = !it.completed!!))) },
            onEditButtonClick = { navigator.navigateToRefactorFragment(it.id) },
            onDeleteButtonClick = { viewModel.onUiAction(UiAction.DeleteTodoItem(it)) }
        )
    }
}