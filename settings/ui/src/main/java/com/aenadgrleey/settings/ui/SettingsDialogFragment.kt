package com.aenadgrleey.settings.ui

import android.app.Dialog
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.aenadgrleey.core.di.holder.scopedComponent
import com.aenadgrleey.settings.domain.SettingsNavigator
import com.aenadgrleey.settings.ui.composables.SettingsScreen
import com.aenadgrleey.settings.ui.di.SettingUiComponentProvider
import com.aenadgrleey.settings.ui.di.SettingsViewComponent
import com.aenadgrleey.settings.ui.di.SettingsViewComponentProvider
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class SettingsDialogFragment : BottomSheetDialogFragment() {

    private val settingUiComponent by scopedComponent {
        (requireContext().applicationContext as SettingUiComponentProvider).provideSettingsUiComponentProvider()
    }

    private var settingsViewComponent: SettingsViewComponent? = null

    private val viewModel by viewModels<SettingsViewModel> {
        settingUiComponent.viewModelFactory()
    }

    var navigator: SettingsNavigator? = null
        @Inject set

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        settingsViewComponent = (requireActivity() as SettingsViewComponentProvider).provideSettingsViewComponent()

        settingsViewComponent!!.inject(this)

        return super.onCreateDialog(savedInstanceState).apply {
            (this as BottomSheetDialog).setContentView(
                ComposeView(requireContext()).apply {
                    setContent {
                        Mdc3Theme {
                            Column {
                                BottomSheetDefaults.DragHandle(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    width = 64.dp
                                )

                                SettingsScreen(
                                    onUiAction = viewModel::onUiAction,
                                    uiEventsFlow = viewModel.uiEvents,
                                    uiStateFlow = viewModel.uiState,
                                    navigator = navigator!!,
                                    lifecycleOwner = this@SettingsDialogFragment
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(32.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingsViewComponent = null
        navigator = null
    }
}