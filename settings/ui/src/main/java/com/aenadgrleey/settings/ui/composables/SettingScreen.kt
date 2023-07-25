package com.aenadgrleey.settings.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aenadgrleey.settings.domain.SettingsNavigator
import com.aenadgrleey.settings.domain.model.AppTheme
import com.aenadgrleey.settings.ui.model.UiAction
import com.aenadgrleey.settings.ui.model.UiEvent
import com.aenadgrleey.settings.ui.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import com.aenadgrleey.resources.R as CommonR

@Composable
fun SettingsScreen(
    onUiAction: (UiAction) -> Unit,
    uiEventsFlow: Flow<UiEvent>,
    uiStateFlow: StateFlow<UiState>,
    navigator: SettingsNavigator,
    lifecycleOwner: LifecycleOwner,
) {
    val uiState by uiStateFlow.collectAsStateWithLifecycle(lifecycleOwner, Lifecycle.State.RESUMED)
    LaunchedEffect(key1 = Unit) {
        uiEventsFlow.collect {
            when (it) {
                UiEvent.ExitRequest -> navigator.exitSettings()
            }
        }
    }
    SettingsScreen(
        uiState = uiState,
        onUiAction = onUiAction
    )
}

@Composable
fun SettingsScreen(uiState: UiState, onUiAction: (UiAction) -> Unit) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = LocalContext.current.resources.getString(CommonR.string.selectTheme),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
        SettingScreenThemeSelector(currentTheme = uiState.appTheme, onUiAction = onUiAction)
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
        )
        Text(
            text = LocalContext.current.resources.getString(CommonR.string.authorization),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
        SignOutButton(onUiAction = onUiAction)
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
    ) {
        SettingsScreen(uiState = UiState(AppTheme.Dark)) {

        }
    }
}