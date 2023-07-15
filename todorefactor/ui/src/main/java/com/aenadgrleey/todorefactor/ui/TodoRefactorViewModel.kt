package com.aenadgrleey.todorefactor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aenadgrleey.core.domain.Importance
import com.aenadgrleey.tododomain.repository.TodoItemRepository
import com.aenadgrleey.todorefactor.domain.TodoItemId
import com.aenadgrleey.todorefactor.ui.model.TodoItemDataToUiStateMapper
import com.aenadgrleey.todorefactor.ui.model.UiAction
import com.aenadgrleey.todorefactor.ui.model.UiEvent
import com.aenadgrleey.todorefactor.ui.model.UiState
import com.aenadgrleey.todorefactor.ui.model.UiStateToTodoItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import javax.inject.Provider

@OptIn(FlowPreview::class)
class TodoRefactorViewModel @Inject constructor(
    @TodoItemId todoItemId: String?,
    private val repository: TodoItemRepository,
) : ViewModel() {

    private val todoItemDataToUiStateMapper = TodoItemDataToUiStateMapper()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (todoItemId != null)
                mUiState.value = todoItemDataToUiStateMapper.map(repository.todoItem(todoItemId))
            else
                mUiState.value = UiState(
                    id = null,
                    lastModifiedBy = null,
                    created = null,
                    lastModified = null
                )
        }
    }

    private val uiStateDataMapper = UiStateToTodoItemData()
    private val dataUiStateMapper = TodoItemDataToUiStateMapper()


    val uiEvents: Flow<UiEvent> get() = mUiEvents.receiveAsFlow()
    private var mUiEvents = Channel<UiEvent>()
    val uiState: StateFlow<UiState?> get() = mUiState
    private var mUiState = MutableStateFlow<UiState?>(null)

    fun onUiAction(uiAction: UiAction) {
        viewModelScope.launch {
            when (uiAction) {
                is UiAction.OnImportanceChange -> changeImportance(uiAction.importance)
                is UiAction.OnTextChange -> changeText(uiAction.text)
                is UiAction.OnCompletenessChange -> changeCompleteness(uiAction.completeness)
                is UiAction.OnDeadlineChange -> changeDeadline(uiAction.enabled, uiAction.deadline)
                UiAction.OnSaveRequest -> saveTodoItem()
                UiAction.OnDeleteRequest -> deleteTodoItem()
                UiAction.OnExitRequest -> mUiEvents.send(UiEvent.ExitRequest)
            }
        }
    }

    private fun changeText(text: String) {
        mUiState.value = mUiState.value!!.copy(body = text)
    }

    private fun changeImportance(importance: Importance) {
        mUiState.value = mUiState.value!!.copy(importance = importance)
    }

    private fun changeCompleteness(completeness: Boolean) {
        mUiState.value = mUiState.value!!.copy(completeness = completeness)
    }

    private fun changeDeadline(enabled: Boolean, deadline: Date?) {
        println("$enabled $deadline")
        mUiState.value = mUiState.value!!.copy(deadlineEnabled = enabled, deadline = deadline)
    }

    private fun saveTodoItem() {
        mUiState.value?.let {
            viewModelScope.launch {
                repository.addTodoItem(uiStateDataMapper.map(it))
                mUiEvents.send(UiEvent.ExitRequest)
            }
        }
    }

    private fun deleteTodoItem() {
        mUiState.value?.let {
            viewModelScope.launch {
                repository.deleteTodoItem(uiStateDataMapper.map(it))
                mUiEvents.send(UiEvent.ExitRequest)
            }
        }
    }


    class ViewModelFactory @Inject constructor(
        sharedViewModelProvider: Provider<TodoRefactorViewModel>,
    ) : ViewModelProvider.Factory {

        private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
            TodoRefactorViewModel::class.java to sharedViewModelProvider
        )

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return providers[modelClass]!!.get() as T
        }
    }
}