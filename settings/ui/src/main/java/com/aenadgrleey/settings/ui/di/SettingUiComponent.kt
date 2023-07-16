package com.aenadgrleey.settings.ui.di

import com.aenadgrleey.core.di.FeatureScope
import com.aenadgrleey.settings.ui.SettingDialogFragment
import com.aenadgrleey.settings.ui.SettingsViewModel
import dagger.Subcomponent

@FeatureScope
@Subcomponent()
interface SettingUiComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingUiComponent
    }

    fun viewModelFactory(): SettingsViewModel.ViewModelFactory

    fun inject(fragment: SettingDialogFragment)
}

interface SettingUiComponentProvider {
    fun provideSettingsUiComponentProvider(): SettingUiComponent
}