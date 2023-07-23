package com.aenadgrleey.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aenadgrleey.core.domain.exceptions.ServerErrorException
import com.aenadgrleey.core.domain.exceptions.WrongAuthorizationException
import com.aenadgrleey.list.ui.model.TodoItem
import com.aenadgrleey.list.ui.model.TodoItemDataToTodoItem
import com.aenadgrleey.list.ui.model.TodoItemToTodoItemData
import com.aenadgrleey.list.ui.model.UiAction
import com.aenadgrleey.list.ui.model.UiEvent
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
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
    val swipeRefreshEvents get() = mSwipeRefreshEvents.receiveAsFlow()
    private val mSwipeRefreshEvents = Channel<UiEvent>()
    val coordinatorEvents get() = mCoordinatorEvents.receiveAsFlow()
    private val mCoordinatorEvents = Channel<UiEvent>()
    val recyclerEvents get() = mRecyclerEvents.receiveAsFlow()
    private val mRecyclerEvents = Channel<UiEvent.RecyclerEvent>()
    val completedCount: StateFlow<Int> get() = mCompletedCount
    private val mCompletedCount = MutableStateFlow(0)


    init {
        viewModelScope.launch {
            mShowCompleted.collectLatest {
                repository.todoItems(mShowCompleted.value).debounce(100).collectLatest {
                    mTodoItems.value = it.map(dataPresenterMapper::map)
                }
            }
        }

        viewModelScope.launch {
            repository.completedItemsCount().debounce(100).collectLatest {
                mCompletedCount.value = it
            }
        }
    }


    fun onUiAction(uiAction: UiAction) {
        viewModelScope.launch(Dispatchers.IO) {
            when (uiAction) {
                UiAction.SmoothScrollUpRequest -> mRecyclerEvents.send(UiEvent.RecyclerEvent.ScrollUp)
                UiAction.ImmediateScrollUpRequest -> mRecyclerEvents.send(UiEvent.RecyclerEvent.ImmediateScrollUp)
                is UiAction.AddTodoItem -> runGuaranteedIOOperation { repository.addTodoItem(presenterDataMapper.map(uiAction.todoItem)) }
                is UiAction.DeleteTodoItem -> runGuaranteedIOOperation { repository.deleteTodoItem(presenterDataMapper.map(uiAction.todoItem)) }
                UiAction.ToggledCompletedMark -> mShowCompleted.value = !mShowCompleted.value
                UiAction.RefreshTodoItems -> {
                    mSwipeRefreshEvents.send(UiEvent.SyncingWithServer)
                    mCoordinatorEvents.send(UiEvent.SyncingWithServer)
                    runGuaranteedIOOperation { repository.fetchRemoteData() }
                }
            }
        }
    }

    //all operations may be canceled, because scope may be canceled, but we really don't want that,
    // so let's run it in the app scope
    private fun runGuaranteedIOOperation(block: suspend () -> Unit) {
        CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) { catchRemoteExceptions(block::invoke) }
    }

    private suspend fun catchRemoteExceptions(block: suspend () -> Unit) {
        try {
            block.invoke()
        } catch (unknownHostException: java.net.UnknownHostException) {
            mSwipeRefreshEvents.send(UiEvent.ConnectionError)
            mCoordinatorEvents.send(UiEvent.ConnectionError)
        } catch (authErrorException: WrongAuthorizationException) {
            mSwipeRefreshEvents.send(UiEvent.BadServerResponse)
            mCoordinatorEvents.send(UiEvent.BadServerResponse)
        } catch (serverErrorException: ServerErrorException) {
            mSwipeRefreshEvents.send(UiEvent.BadServerResponse)
            mCoordinatorEvents.send(UiEvent.BadServerResponse)
        } catch (socketTimeoutException: SocketTimeoutException) {
            mSwipeRefreshEvents.send(UiEvent.BadServerResponse)
            mCoordinatorEvents.send(UiEvent.BadServerResponse)
        } finally {
            mSwipeRefreshEvents.send(UiEvent.SyncedWithServer)
            mCoordinatorEvents.send(UiEvent.SyncedWithServer)
        }

    }

    class ViewModelFactory @Inject constructor(
        viewModelProvider: Provider<TodoListViewModel>,
    ) : ViewModelProvider.Factory {

        private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
            TodoListViewModel::class.java to viewModelProvider
        )

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return providers[modelClass]!!.get() as T
        }
    }
}