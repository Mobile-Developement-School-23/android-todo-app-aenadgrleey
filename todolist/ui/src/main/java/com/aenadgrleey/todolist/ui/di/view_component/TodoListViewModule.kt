package com.aenadgrleey.todolist.ui.di.view_component

import com.aenadgrleey.core.di.FeatureScope
import com.aenadgrleey.todolist.domain.TodoListNavigator
import com.aenadgrleey.todolist.ui.TodoListViewModel
import com.aenadgrleey.todolist.ui.model.UiAction
import com.aenadgrleey.todolist.ui.utils.TodoItemsRecyclerViewAdapter
import dagger.Module
import dagger.Provides

@Module
object TodoListViewModule {
    @FeatureScope
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