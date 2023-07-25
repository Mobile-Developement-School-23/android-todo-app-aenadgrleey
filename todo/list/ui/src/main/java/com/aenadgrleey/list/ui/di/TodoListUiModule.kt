package com.aenadgrleey.list.ui.di

import com.aenadgrleey.list.ui.model.TodoItem
import com.aenadgrleey.list.ui.model.TodoItemDataToTodoItem
import com.aenadgrleey.list.ui.model.TodoItemToTodoItemData
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todo.domain.utils.Mapper
import dagger.Module
import dagger.Provides

@Module(subcomponents = [TodoListUiComponent::class])
object TodoListUiModule {
    @Provides
    fun providesTodoItemUiToDataMapper(): Mapper<TodoItem, TodoItemData> = TodoItemToTodoItemData()

    @Provides
    fun providesTodoItemDataToUiMapper(): Mapper<TodoItemData, TodoItem> = TodoItemDataToTodoItem()
}