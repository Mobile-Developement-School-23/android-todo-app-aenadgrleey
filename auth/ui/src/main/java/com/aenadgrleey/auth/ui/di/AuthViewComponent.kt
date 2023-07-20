package com.aenadgrleey.auth.ui.di

import com.aenadgrleey.auth.ui.AuthFragment
import dagger.Subcomponent

@Subcomponent
interface AuthViewComponent {

    fun inject(authFragment: AuthFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthViewComponent
    }
}

interface AuthViewComponentProvider {
    fun provideAuthViewComponent(): AuthViewComponent
}