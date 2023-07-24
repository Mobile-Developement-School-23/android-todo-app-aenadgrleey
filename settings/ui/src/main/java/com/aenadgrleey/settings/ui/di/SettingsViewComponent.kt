package com.aenadgrleey.settings.ui.di

import com.aenadgrleey.settings.ui.SettingsDialogFragment
import dagger.Subcomponent

@Subcomponent
interface SettingsViewComponent {

    fun inject(settingsDialogFragment: SettingsDialogFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsViewComponent
    }
}

interface SettingsViewComponentProvider {
    fun provideSettingsViewComponent(): SettingsViewComponent
}