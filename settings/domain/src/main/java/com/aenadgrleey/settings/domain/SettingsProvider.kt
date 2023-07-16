package com.aenadgrleey.settings.domain

import com.aenadgrleey.settings.domain.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsProvider {
    suspend fun settingsFlow(): Flow<AppSettings>
    suspend fun updateAppSettings(appSettings: AppSettings?)
}