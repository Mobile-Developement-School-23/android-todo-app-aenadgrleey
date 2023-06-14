package com.aenadgrleey.tobedone.presentation.catalogue

import androidx.lifecycle.ViewModel
import com.aenadgrleey.tobedone.data.TodoItemRepository
import com.aenadgrleey.tobedone.presentation.TodoItem
import com.aenadgrleey.tobedone.presentation.TodoItemDataToTodoItem
import com.aenadgrleey.tobedone.utils.ListMapperImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CatalogueFragmentViewModel @Inject constructor(repository: TodoItemRepository) : ViewModel() {
    val items: Flow<List<TodoItem>> = repository.todoItems()
        .map { ListMapperImpl(mapper = TodoItemDataToTodoItem()).map(it) }
}