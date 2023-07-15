package com.aenadgrleey.auth.ui.di

import com.aenadgrleey.auth.ui.AuthFragment
import com.aenadgrleey.auth.ui.AuthFragmentViewModel
import com.aenadgrleey.di.FeatureScope
import dagger.Subcomponent

@FeatureScope
@Subcomponent()
interface AuthUiComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthUiComponent
    }

    fun viewModelFactory(): AuthFragmentViewModel.ViewModelFactory

    fun inject(fragment: AuthFragment)
}

interface AuthUiComponentProvider {
    fun provideAuthComponentProvider(): AuthUiComponent
}