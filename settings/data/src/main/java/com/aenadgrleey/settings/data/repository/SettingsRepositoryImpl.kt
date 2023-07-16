package com.aenadgrleey.settings.data.repository

import com.aenadgrleey.settings.domain.SettingsProvider
import com.aenadgrleey.settings.domain.SettingsRepository
import com.aenadgrleey.settings.domain.model.AppSettings
import com.aenadgrleey.settings.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val settingsProvider: SettingsProvider) : SettingsRepository {
    override suspend fun settingFlow(): Flow<AppSettings> = settingsProvider.settingsFlow()

    override suspend fun updateTheme(appTheme: AppTheme) {
        settingsProvider.updateAppSettings(settingsProvider.settingsFlow().first().copy(appTheme))
    }

    override suspend fun clear() {
        settingsProvider.updateAppSettings(null)
    }
}