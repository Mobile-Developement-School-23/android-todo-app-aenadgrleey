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
import androidx.fragment.app.activityViewModels
import com.aenadgrleey.settings.domain.SettingsNavigator
import com.aenadgrleey.settings.ui.composables.SettingsScreen
import com.aenadgrleey.settings.ui.di.SettingUiComponentProvider
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class SettingDialogFragment : BottomSheetDialogFragment() {
    private val settingUiComponent by lazy {
        (requireActivity() as SettingUiComponentProvider).provideSettingsUiComponentProvider()
    }
    private val viewModel by activityViewModels<SettingsViewModel> {
        settingUiComponent.viewModelFactory()
    }

    @Inject
    lateinit var navigator: SettingsNavigator

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        settingUiComponent.inject(this)
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

                                SettingsScreen(viewModel::onUiAction, viewModel.uiEvents, viewModel.uiState, navigator, this@SettingDialogFragment)

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
}