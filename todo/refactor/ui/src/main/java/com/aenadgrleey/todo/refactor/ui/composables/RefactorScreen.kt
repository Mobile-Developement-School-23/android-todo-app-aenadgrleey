package com.aenadgrleey.todo.refactor.ui.composables

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aenadgrleey.resources.R.string
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.refactor.domain.TodoRefactorNavigator
import com.aenadgrleey.todo.refactor.ui.model.UiAction
import com.aenadgrleey.todo.refactor.ui.model.UiEvent
import com.aenadgrleey.todo.refactor.ui.model.UiState
import com.aenadgrleey.todo.refactor.ui.utils.Res
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun RefactorScreen(
    lifecycleOwner: LifecycleOwner,
    uiEvents: Flow<UiEvent>,
    onUiAction: (UiAction) -> Unit,
    uiStateFlow: StateFlow<UiState>,
    navigator: TodoRefactorNavigator,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    var snackBarJob: Job = remember { Job() }
    val savedTitle = Res().getString(string.saved)
    LaunchedEffect(key1 = Unit) {
        uiEvents.collectLatest {
            when (it) {
                UiEvent.TodoItemSaved -> {
                    snackBarJob.cancel()
                    snackBarJob = snackBarScope.launch { snackbarHostState.showSnackbar(savedTitle) }
                }

                UiEvent.ExitRequest -> {
                    onUiAction(UiAction.ResetTodoItem)
                    navigator.exitRefactor()
                }
            }
        }
    }
    val uiState by uiStateFlow.collectAsStateWithLifecycle(lifecycleOwner)
    RefactorScreen(uiState = uiState, onUiAction = onUiAction, snackbarHostState)
//    else
//  here should be some loading screen???

}

@Composable
fun RefactorScreen(
    uiState: UiState,
    onUiAction: (UiAction) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            RefactorScreenToolbar(
                dependentScrollState = scrollState,
                completeness = uiState.completeness,
                onUiAction
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
                onUiAction = onUiAction
            )

            RefactorScreenImportanceSelector(
                selected = uiState.importance,
                onUiAction = onUiAction
            )

            Divider(Modifier.fillMaxWidth())

            RefactorScreenDeadlineSelector(
                enabled = uiState.deadlineEnabled,
                date = uiState.deadline,
                onUiAction = onUiAction
            )

            Divider(Modifier.fillMaxWidth())

            RefactorScreenDeleteButton(onUiAction = onUiAction)

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
            text = LocalContext.current.resources.getString(string.lorem_ipsum),
            created = Date(),
            id = "aaaa",
            importance = Importance.Common,
            lastModified = Date(),
            lastModifiedBy = "aaa"
        ),
        onUiAction = {},
        snackbarHostState = SnackbarHostState()
    )
}