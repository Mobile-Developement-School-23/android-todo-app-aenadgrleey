package com.aenadgrleey.todo.refactor.ui.di

import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todo.domain.utils.Mapper
import com.aenadgrleey.todo.refactor.ui.model.TodoItemDataToUiStateMapper
import com.aenadgrleey.todo.refactor.ui.model.UiState
import com.aenadgrleey.todo.refactor.ui.model.UiStateToTodoItemData
import dagger.Module
import dagger.Provides

@Module(subcomponents = [TodoRefactorUiComponent::class])
object TodoRefactorUiModule {
    @Provides
    fun providesTodoItemDataToUiStateMapper(): Mapper<TodoItemData?, UiState> = TodoItemDataToUiStateMapper()

    @Provides
    fun providesUiStateToTodoItemDataMapper(): Mapper<UiState, TodoItemData> = UiStateToTodoItemData()
}