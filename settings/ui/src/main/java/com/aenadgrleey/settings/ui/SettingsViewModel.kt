package com.aenadgrleey.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aenadgrleey.auth.domain.AuthRepository
import com.aenadgrleey.settings.domain.SettingsRepository
import com.aenadgrleey.settings.domain.model.AppTheme
import com.aenadgrleey.settings.ui.model.UiAction
import com.aenadgrleey.settings.ui.model.UiEvent
import com.aenadgrleey.settings.ui.model.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val uiState: StateFlow<UiState> get() = mUiState
    private val mUiState = MutableStateFlow(UiState(AppTheme.System))

    val uiEvents: Flow<UiEvent> get() = mUiEvents.receiveAsFlow()
    private val mUiEvents = Channel<UiEvent>()

    fun onUiAction(uiAction: UiAction) {
        viewModelScope.launch(Dispatchers.IO) {
            when (uiAction) {
                UiAction.OnSignOut -> {
                    mUiEvents.send(UiEvent.ExitRequest)
                    delay(500)
                    authRepository.signOut()
                }

                is UiAction.OnThemeSelect -> {
                    settingsRepository.updateTheme(uiAction.theme)
                    changeTheme(uiAction.theme)
                }
            }
        }
    }

    private fun changeTheme(theme: AppTheme) {
        mUiState.value = UiState(theme)
    }

    class ViewModelFactory @Inject constructor(
        myViewModelProvider: Provider<SettingsViewModel>,
    ) : ViewModelProvider.Factory {
        private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
            SettingsViewModel::class.java to myViewModelProvider
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return providers[modelClass]!!.get() as T
        }
    }
}