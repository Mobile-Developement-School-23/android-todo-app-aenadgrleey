package com.aenadgrleey.auth.ui.di

import com.aenadgrleey.auth.ui.AuthFragment
import com.aenadgrleey.auth.ui.AuthFragmentNavigator
import com.aenadgrleey.auth.ui.AuthFragmentViewModel
import com.aenadgrleey.di.FeatureScope
import dagger.BindsInstance
import dagger.Subcomponent

@FeatureScope
@Subcomponent()
interface AuthUiComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance navigator: AuthFragmentNavigator): AuthUiComponent
    }

    fun viewModelFactory(): AuthFragmentViewModel.ViewModelFactory

    fun inject(fragment: AuthFragment)
}

interface AuthComponentProvider {
    fun provideAuthComponentProvider(): AuthUiComponent
}