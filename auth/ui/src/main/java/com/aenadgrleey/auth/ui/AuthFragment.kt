package com.aenadgrleey.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aenadgrleey.auth.domain.AuthNavigator
import com.aenadgrleey.auth.ui.composables.AuthScreen
import com.aenadgrleey.auth.ui.di.AuthUiComponentProvider
import com.aenadgrleey.auth.ui.di.AuthViewComponent
import com.aenadgrleey.auth.ui.di.AuthViewComponentProvider
import com.aenadgrleey.core.di.holder.scopedComponent
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.transition.platform.MaterialSharedAxis
import javax.inject.Inject

class AuthFragment : Fragment() {

    private val authUiComponent by scopedComponent {
        (requireContext().applicationContext as AuthUiComponentProvider).provideAuthComponentProvider()
    }

    private var authViewComponent: AuthViewComponent? = null

    private val viewModel: AuthFragmentViewModel by viewModels {
        authUiComponent.viewModelFactory()
    }


    var navigator: AuthNavigator? = null
        @Inject set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animDuration = resources
            .getInteger(com.google.android.material.R.integer.m3_sys_motion_duration_long4).toLong()
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            .apply { duration = animDuration }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            .apply { duration = animDuration }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        authViewComponent = (requireActivity() as AuthViewComponentProvider).provideAuthViewComponent()

        authViewComponent!!.inject(this)

        return ComposeView(context = requireContext()).apply {
            setContent {
                Mdc3Theme {
                    AuthScreen(
                        uiEvents = viewModel.uiEvents,
                        onUiAction = viewModel::onUiAction,
                        navigator = navigator!!
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        authViewComponent = null
        navigator = null
    }
}