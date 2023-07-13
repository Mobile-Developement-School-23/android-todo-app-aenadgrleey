package com.aenadgrleey.tobedone.presentation.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aenadgrleey.data.models.TodoItemData
import com.aenadgrleey.data.remote.NetworkStatus
import com.aenadgrleey.repositories.TodoItemRepository
import com.aenadgrleey.tobedone.di.ApplicationScope
import com.aenadgrleey.tobedone.presentation.models.TodoItem
import com.aenadgrleey.tobedone.utils.Mapper
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@ApplicationScope
@OptIn(FlowPreview::class)
class SharedViewModel @Inject constructor(
    private val repository: TodoItemRepository,
    private val dataPresenterMapper: Mapper<TodoItemData, TodoItem>,
    private val presenterDataMapper: Mapper<TodoItem, TodoItemData>,
) : ViewModel() {

    val todoItems get() = mTodoItem
    private val mTodoItem = MutableStateFlow<List<TodoItem>>(listOf())

    val showCompleted: StateFlow<Boolean> get() = mShowCompleted
    private var mShowCompleted = MutableStateFlow(false)

    val completedCount = repository.completedItemsCount().debounce(100)

    val networkStatus get() = mNetworkStatus
    private val mNetworkStatus = MutableStateFlow(NetworkStatus.PENDING)


    init {
        viewModelScope.launch {
            mShowCompleted.collectLatest {
                repository.todoItems(mShowCompleted.value).debounce(100).collectLatest {
                    todoItems.value = it.map(dataPresenterMapper::map)
                }
            }
        }
        viewModelScope.launch {
            repository.networkStatus.collect {
                //that's called crutch
                if (it != NetworkStatus.PENDING) delay(100)
                mNetworkStatus.value = it
            }
        }
    }

    fun toggleShowCompleted() {
        viewModelScope.launch { mShowCompleted.value = !mShowCompleted.value }
    }

    fun addTodoItem(todoItem: TodoItem) {
        viewModelScope.launch { repository.addTodoItem(presenterDataMapper.map(todoItem)) }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch { repository.deleteTodoItem(presenterDataMapper.map(todoItem)) }
    }

    fun fetchRemoteData() {
        viewModelScope.launch { repository.fetchRemoteData() }
    }

    class ViewModelFactory @Inject constructor(
        sharedViewModelProvider: Provider<SharedViewModel>,
    ) : ViewModelProvider.Factory {

        private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
            SharedViewModel::class.java to sharedViewModelProvider
        )

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return providers[modelClass]!!.get() as T
        }
    }
}