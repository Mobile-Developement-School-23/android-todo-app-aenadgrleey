package com.aenadgrleey.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aenadgrleey.list.ui.model.TodoItem
import com.aenadgrleey.list.ui.model.TodoItemDataToTodoItem
import com.aenadgrleey.list.ui.model.TodoItemToTodoItemData
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.list.ui.model.UiEvent
import com.aenadgrleey.todo.domain.models.NetworkStatus
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class TodoListViewModel @Inject constructor(
    private val repository: TodoItemRepository,
) : ViewModel() {


    private val dataPresenterMapper = TodoItemDataToTodoItem()
    private val presenterDataMapper = TodoItemToTodoItemData()

    val todoItems get() = mTodoItems
    private val mTodoItems = MutableStateFlow<List<TodoItem>>(listOf())

    val isShowingCompleted: StateFlow<Boolean> get() = mShowCompleted.asStateFlow()
    private var mShowCompleted = MutableStateFlow(false)
    val swipeRefreshEvents get() = mSwipeRefreshEvents.receiveAsFlow().onEach { println(it) }
    private val mSwipeRefreshEvents = Channel<UiEvent>()
    val coordinatorEvents get() = mCoordinatorEvents.receiveAsFlow()
    private val mCoordinatorEvents = Channel<UiEvent>()
    val recyclerEvents get() = mRecyclerEvents.receiveAsFlow()
    private val mRecyclerEvents = Channel<UiEvent.RecyclerEvent>()


    val completedCount: StateFlow<Int> get() = mCompletedCount
    private val mCompletedCount = MutableStateFlow(0)


    init {
        println("New TodoListViewModel")

        viewModelScope.launch {
            mShowCompleted.collectLatest {
                repository.todoItems(mShowCompleted.value).debounce(100).collectLatest {
                    mTodoItems.value = it.map(dataPresenterMapper::map)
                }
            }
        }

        viewModelScope.launch {
            repository.completedItemsCount().debounce(0).collectLatest {
                mCompletedCount.value = it
            }
        }
        viewModelScope.launch(Dispatchers.Main) {
            repository.networkStatus.collectLatest {
                when (it) {
                    NetworkStatus.SYNCED -> {
                        mSwipeRefreshEvents.send(UiEvent.SyncedWithServer)
                        mCoordinatorEvents.send(UiEvent.SyncedWithServer)
                    }

                    NetworkStatus.NO_INTERNET -> {
                        mSwipeRefreshEvents.send(UiEvent.ConnectionError)
                        mCoordinatorEvents.send(UiEvent.ConnectionError)
                    }

                    NetworkStatus.SERVER_INTERNAL_ERROR -> {
                        mSwipeRefreshEvents.send(UiEvent.BadServerResponse)
                        mCoordinatorEvents.send(UiEvent.BadServerResponse)
                    }

                    NetworkStatus.SERVER_ERROR -> {
                        mSwipeRefreshEvents.send(UiEvent.BadServerResponse)
                        mCoordinatorEvents.send(UiEvent.BadServerResponse)
                    }

                    NetworkStatus.SYNCING -> {
                        mSwipeRefreshEvents.send(UiEvent.SyncingWithServer)
                        mCoordinatorEvents.send(UiEvent.SyncingWithServer)
                    }
                }

            }
        }
    }


    fun onUiAction(uiAction: UiAction) {
        viewModelScope.launch(Dispatchers.IO) {
            when (uiAction) {
                UiAction.ScrollUpRequest -> mRecyclerEvents.send(UiEvent.RecyclerEvent.ScrollUp)
                is UiAction.AddTodoItem -> repository.addTodoItem(presenterDataMapper.map(uiAction.todoItem))
                is UiAction.DeleteTodoItem -> repository.deleteTodoItem(presenterDataMapper.map(uiAction.todoItem))
                UiAction.RefreshTodoItems -> repository.fetchRemoteData()
                UiAction.ToggledCompletedMark -> mShowCompleted.value = !mShowCompleted.value
            }
        }
    }

    class ViewModelFactory @Inject constructor(
        viewModelProvider: Provider<TodoListViewModel>,
    ) : ViewModelProvider.Factory {

        init {
            println("New TodoListViewModel.Factory")
        }

        private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
            TodoListViewModel::class.java to viewModelProvider
        )

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return providers[modelClass]!!.get() as T
        }
    }
}