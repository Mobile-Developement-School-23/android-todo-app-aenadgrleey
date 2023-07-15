package com.aenadgrleey.todorefactor.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aenadgrleey.core.domain.Importance
import com.aenadgrleey.core.domain.NetworkStatus
import com.aenadgrleey.core.domain.models.TodoItemData
import com.aenadgrleey.resources.R.string
import com.aenadgrleey.tododomain.repository.TodoItemRepository
import com.aenadgrleey.todorefactor.ui.TodoRefactorViewModel
import com.aenadgrleey.todorefactor.ui.model.UiAction
import com.aenadgrleey.todorefactor.ui.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Date

@Composable
fun RefactorScreen(
    lifecycle: Lifecycle,
    viewModel: TodoRefactorViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(lifecycle)
    if (uiState != null) RefactorScreen(uiState = uiState!!, viewModel = viewModel)
//    else
//  here should be some loading screen???

}

@Composable
fun RefactorScreen(
    uiState: UiState,
    viewModel: TodoRefactorViewModel,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RefactorScreenToolbar(
                dependentScrollState = scrollState,
                completeness = uiState.completeness,
                onNavIconClick = { viewModel.onUiAction(UiAction.OnExitRequest) },
                onCompletenessIconClick = { viewModel.onUiAction(UiAction.OnCompletenessChange(!uiState.completeness)) },
                onSaveIconClick = { viewModel.onUiAction(UiAction.OnSaveRequest) }
            )
        },
    ) {
        Column(
            Modifier
                .verticalScroll(scrollState)
                .padding(it)
                .padding(horizontal = 12.dp)
                .background(MaterialTheme.colorScheme.background),
        ) {

            RefactorScreenTextField(
                text = uiState.text ?: "",
                error = uiState.textError,
                onValueChange = { value -> viewModel.onUiAction(UiAction.OnTextChange(value)) }
            )

            RefactorScreenImportanceChooser(
                selected = uiState.importance,
                onChoose = { importance -> viewModel.onUiAction(UiAction.OnImportanceChange(importance)) }
            )

            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            RefactorScreenDeadlineSelector(
                enabled = uiState.deadlineEnabled,
                value = uiState.deadline,
                onDateChange = { deadline -> viewModel.onUiAction(UiAction.OnDeadlineChange(enabled = uiState.deadlineEnabled, deadline = deadline)) },
                onToggle = { deadlineEnabled -> viewModel.onUiAction(UiAction.OnDeadlineChange(enabled = deadlineEnabled, deadline = uiState.deadline)) }
            )

            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            RefactorScreenDeleteButton(
                onClick = { viewModel.onUiAction(UiAction.OnDeleteRequest) }
            )
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp)
            )
        }
    }
}

@Preview
@Composable
fun RefactorPreview() {
    RefactorScreen(
        uiState = UiState(
            text = LocalContext.current.resources.getString(string.lorem_ipsum), created = Date(), id = "aaaa", importance = Importance.Common, lastModified = Date(), lastModifiedBy = "aaa"
        ), TodoRefactorViewModel("aa", object : TodoItemRepository {
            override fun todoItems(includeCompleted: Boolean) = flowOf<List<TodoItemData>>()
            override suspend fun todoItem(id: String) = TodoItemData("aaa")
            override fun completedItemsCount(): Flow<Int> = flowOf(5)
            override val networkStatus: Flow<NetworkStatus> get() = flowOf(NetworkStatus.SYNCED)
            override suspend fun addTodoItem(todoItem: TodoItemData) {}
            override suspend fun deleteTodoItem(todoItem: TodoItemData) {}
            override suspend fun fetchRemoteData() {}

        })
    )
}