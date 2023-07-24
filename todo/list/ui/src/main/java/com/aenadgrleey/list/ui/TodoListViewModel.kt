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
    private val mShowCompleted = MutableStateFlow(false)

    val completedCount: StateFlow<Int> get() = mCompletedCount
    private val mCompletedCount = MutableStateFlow(0)

    val uiEvents get() = mUiEvents.receiveAsFlow()
    private val mUiEvents = Channel<UiEvent>()


    init {
        viewModelScope.launch {
            mShowCompleted.collectLatest {
                repository.todoItems(it).debounce(100).collectLatest { items ->
                    mTodoItems.value = items.map(dataPresenterMapper::map)
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
                UiAction.SmoothScrollUpRequest -> mUiEvents.send(UiEvent.ScrollUp)
                UiAction.ImmediateScrollUpRequest -> mUiEvents.send(UiEvent.ImmediateScrollUp)
                is UiAction.AddTodoItem -> runGuaranteedIOOperation { repository.addTodoItem(presenterDataMapper.map(uiAction.todoItem)) }
                UiAction.ToggledCompletedMark -> {
                    mShowCompleted.value = !mShowCompleted.value
                    mUiEvents.send(UiEvent.VisibilityChange(mShowCompleted.value))
                }

                is UiAction.UndoDeleteTodoItem -> runGuaranteedIOOperation { repository.addTodoItem(presenterDataMapper.map(uiAction.todoItem)) }
                is UiAction.DeleteTodoItem -> {
                    mUiEvents.send(UiEvent.ShowDeletedItem(uiAction.todoItem))
                    runGuaranteedIOOperation { repository.deleteTodoItem(presenterDataMapper.map(uiAction.todoItem)) }
                }

                UiAction.RefreshTodoItems -> {
                    mUiEvents.send(UiEvent.SyncingWithServer)
                    runGuaranteedIOOperation {
                        repository.fetchRemoteData()
                        //it will be called only if fetch succeeds
                        mUiEvents.send(UiEvent.SyncedWithServer)
                    }
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
            mUiEvents.send(UiEvent.ConnectionError)
        } catch (authErrorException: WrongAuthorizationException) {
            mUiEvents.send(UiEvent.BadServerResponse)
        } catch (serverErrorException: ServerErrorException) {
            mUiEvents.send(UiEvent.BadServerResponse)
        } catch (socketTimeoutException: SocketTimeoutException) {
            mUiEvents.send(UiEvent.BadServerResponse)
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