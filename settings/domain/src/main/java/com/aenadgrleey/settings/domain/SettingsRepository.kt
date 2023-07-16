package com.aenadgrleey.settings.domain

import com.aenadgrleey.settings.domain.model.AppSettings
import com.aenadgrleey.settings.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun settingFlow(): Flow<AppSettings>
    suspend fun updateTheme(appTheme: AppTheme)
    suspend fun clear()
}