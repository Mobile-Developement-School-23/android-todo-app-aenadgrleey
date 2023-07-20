package com.aenadgrleey.auth.ui.di

import com.aenadgrleey.auth.ui.AuthFragmentViewModel
import com.aenadgrleey.core.di.FeatureScope
import dagger.Subcomponent

@FeatureScope
@Subcomponent
interface AuthUiComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthUiComponent
    }

    fun viewModelFactory(): AuthFragmentViewModel.ViewModelFactory

}

interface AuthUiComponentProvider {
    fun provideAuthComponentProvider(): AuthUiComponent
}