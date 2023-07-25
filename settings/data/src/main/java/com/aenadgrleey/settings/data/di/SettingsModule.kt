package com.aenadgrleey.settings.data.di

import com.aenadgrleey.settings.data.provider.SettingsProviderImpl
import com.aenadgrleey.settings.data.repository.SettingsRepositoryImpl
import com.aenadgrleey.settings.domain.SettingsProvider
import com.aenadgrleey.settings.domain.SettingsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class SettingsModule {
    @SettingsProviderScope
    @Binds
    abstract fun bindsSettingsProvider(provider: SettingsProviderImpl): SettingsProvider

    @SettingsProviderScope
    @Binds
    abstract fun bindsSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository
}