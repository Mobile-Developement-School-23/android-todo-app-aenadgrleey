package com.aenadgrleey.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aenadgrleey.auth.domain.AuthRepository
import com.aenadgrleey.auth.ui.models.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class AuthFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    val uiEvents
        get() = mUiState.receiveAsFlow()
    private val mUiState = Channel<UiEvent>()

    fun onAuthRequest() {
        viewModelScope.launch { mUiState.send(UiEvent.AuthRequest) }
    }

    fun onAuthResponse(token: String) {
        viewModelScope.launch {
            authRepository.signIn(token)
            mUiState.send(UiEvent.AuthSuccess)
        }
    }

    class ViewModelFactory @Inject constructor(
        myViewModelProvider: Provider<AuthFragmentViewModel>,
    ) : ViewModelProvider.Factory {
        private val providers = mapOf<Class<*>, Provider<out ViewModel>>(
            AuthFragmentViewModel::class.java to myViewModelProvider
        )

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return providers[modelClass]!!.get() as T
        }
    }

}