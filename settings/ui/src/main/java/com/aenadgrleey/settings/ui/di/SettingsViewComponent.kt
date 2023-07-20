package com.aenadgrleey.settings.ui.di

import com.aenadgrleey.settings.ui.SettingDialogFragment
import dagger.Subcomponent

@Subcomponent
interface SettingsViewComponent {

    fun inject(settingDialogFragment: SettingDialogFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsViewComponent
    }
}

interface SettingsViewComponentProvider {
    fun provideSettingsViewComponent(): SettingsViewComponent
}