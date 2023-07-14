package com.aenadgrleey.todolist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aenadgrleey.core.domain.NetworkStatus
import com.aenadgrleey.tododomain.repository.TodoItemRepository
import com.aenadgrleey.todolist.ui.model.UiAction
import com.aenadgrleey.todolist.ui.model.UiEvent
import com.aenadgrleey.ui.TodoItem
import com.aenadgrleey.ui.TodoItemDataToTodoItem
import com.aenadgrleey.ui.TodoItemToTodoItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@OptIn(FlowPreview::class)
class TodoListViewModel @Inject constructor(
    private val repository: TodoItemRepository,
) : ViewModel() {

    private val dataPresenterMapper = TodoItemDataToTodoItem()
    private val presenterDataMapper = TodoItemToTodoItemData()

    val todoItems get() = mTodoItems
    private val mTodoItems = MutableStateFlow<List<TodoItem>>(listOf())

    val isShowingCompleted: StateFlow<Boolean> get() = mShowCompleted.asStateFlow()
    private var mShowCompleted = MutableStateFlow(false)
    val swipeRefreshEvents: Flow<UiEvent?> get() = mSwipeRefreshEvents.receiveAsFlow()
    private val mSwipeRefreshEvents = Channel<UiEvent>()
    val coordinatorEvents: Flow<UiEvent?> get() = mCoordinatorEvents.receiveAsFlow()
    private val mCoordinatorEvents = Channel<UiEvent>()
    val recyclerEvents: Flow<UiEvent.RecyclerEvent> get() = mRecyclerEvents.receiveAsFlow()
    private val mRecyclerEvents = Channel<UiEvent.RecyclerEvent>()


    val completedCount = repository.completedItemsCount().debounce(100)


    init {
        viewModelScope.launch {
            mShowCompleted.collectLatest {
                repository.todoItems(mShowCompleted.value).debounce(100).collectLatest {
                    mTodoItems.value = it.map(dataPresenterMapper::map)
                }
            }
        }
        viewModelScope.launch(Dispatchers.Main) {
            repository.networkStatus.collect {
                println(it)
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
        viewModelScope.launch(Dispatchers.Main) {
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
        sharedViewModelProvider: Provider<TodoListViewModel>,
    ) : ViewModelProvider.Factory {

        private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
            TodoListViewModel::class.java to sharedViewModelProvider
        )

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return providers[modelClass]!!.get() as T
        }
    }
}