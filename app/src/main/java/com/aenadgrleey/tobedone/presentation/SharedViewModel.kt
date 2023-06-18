package com.aenadgrleey.tobedone.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aenadgrleey.tobedone.data.TodoItemData
import com.aenadgrleey.tobedone.data.TodoItemRepository
import com.aenadgrleey.tobedone.utils.ListMapperImpl
import com.aenadgrleey.tobedone.utils.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: TodoItemRepository,
    private val dataPresenterMapper: Mapper<TodoItemData, TodoItem>,
    private val presenterDataMapper: Mapper<TodoItem, TodoItemData>
) :
    ViewModel() {

    val todoItems
        get() = mTodoItems.map { ListMapperImpl(mapper = dataPresenterMapper).map(it) }
            .flowOn(Dispatchers.IO)

    val showCompleted
        get() = mShowCompleted.onEach { mTodoItems = repository.todoItems(it) }

    private var mShowCompleted = MutableStateFlow(true)
    private var mTodoItems = repository.todoItems(includeCompleted = mShowCompleted.value)

    val completedCount = repository.completedItemsCount()

    fun toggleShowCompleted() {
        viewModelScope.launch(Dispatchers.Default) {
            mShowCompleted.value = !mShowCompleted.value
        }
    }

    fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodoItem(presenterDataMapper.map(todoItem))
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodoItem(presenterDataMapper.map(todoItem))
        }
    }
}