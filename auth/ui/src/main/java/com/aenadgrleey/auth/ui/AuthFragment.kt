package com.aenadgrleey.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aenadgrleey.auth.ui.composables.AuthScreen
import com.aenadgrleey.auth.ui.di.AuthComponentProvider
import com.aenadgrleey.di.holder.scopedComponent
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import javax.inject.Inject

class AuthFragment : Fragment() {

    private val authFragmentComponent by scopedComponent {
        (requireContext().applicationContext as AuthComponentProvider)
            .provideAuthComponentProvider()
    }

    private val viewModel: AuthFragmentViewModel by viewModels {
        authFragmentComponent.viewModelFactory()
    }

    @Inject
    lateinit var navigator: AuthFragmentNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authFragmentComponent.inject(this)
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