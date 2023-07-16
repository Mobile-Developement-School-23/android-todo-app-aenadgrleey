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
import com.aenadgrleey.settings.ui.SettingsViewModel
import com.aenadgrleey.settings.ui.model.UiAction
import com.aenadgrleey.settings.ui.model.UiEvent
import com.aenadgrleey.settings.ui.model.UiState
import com.aenadgrleey.resources.R as CommonR

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigator: SettingsNavigator,
    lifecycleOwner: LifecycleOwner,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(lifecycleOwner, Lifecycle.State.STARTED)
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvents.collect {
            when (it) {
                UiEvent.ExitRequest -> navigator.exitSettings()
            }
        }
    }
    SettingsScreen(
        uiState = uiState,
        onThemeSelect = { viewModel.onUiAction(UiAction.OnThemeSelect(it)) },
        onSignOutButtonClick = { viewModel.onUiAction((UiAction.OnSignOut)) }
    )
}

@Composable
fun SettingsScreen(uiState: UiState, onThemeSelect: (AppTheme) -> Unit, onSignOutButtonClick: () -> Unit) {
    Column(Modifier.padding(horizontal = 10.dp)) {
        Text(
            text = LocalContext.current.resources.getString(CommonR.string.selectTheme),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
        SettingScreenThemeSelector(currentTheme = uiState.appTheme, onSelect = onThemeSelect)
        Spacer(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth()
        )
        Text(
            text = LocalContext.current.resources.getString(CommonR.string.authorization),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .fillMaxWidth()
        )
        SignOutButton(onClick = onSignOutButtonClick)
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
        SettingsScreen(uiState = UiState(AppTheme.Dark), onThemeSelect = {}) {

        }
    }
}