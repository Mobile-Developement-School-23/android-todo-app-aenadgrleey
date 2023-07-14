package com.aenadgrleey.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aenadgrleey.auth.ui.composables.AuthScreen
import com.aenadgrleey.auth.ui.di.AuthUiComponentProvider
import com.aenadgrleey.di.holder.scopedComponent
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.transition.MaterialSharedAxis
import javax.inject.Inject

class AuthFragment : Fragment() {

    private val authUiComponent by scopedComponent {
        (requireActivity() as AuthUiComponentProvider)
            .provideAuthComponentProvider()
    }

    private val viewModel: AuthFragmentViewModel by viewModels {
        authUiComponent.viewModelFactory()
    }

    @Inject
    lateinit var navigator: AuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authUiComponent.inject(this)
        val animDuration = resources
            .getInteger(com.google.android.material.R.integer.m3_sys_motion_duration_long4).toLong()
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            .apply { duration = animDuration }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        ComposeView(context = requireContext()).apply {
            setContent {
                Mdc3Theme {
                    AuthScreen(
                        uiEvents = viewModel.uiEvents,
                        onAuthRequest = viewModel::onAuthRequest,
                        onAuthResponse = viewModel::onAuthResponse,
                        onSuccess = navigator::onSuccessAuth
                    )
                }
            }
        }
}