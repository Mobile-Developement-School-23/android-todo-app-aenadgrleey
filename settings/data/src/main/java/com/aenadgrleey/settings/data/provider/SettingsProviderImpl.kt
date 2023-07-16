package com.aenadgrleey.settings.data.provider

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.settings.data.provider.datastore.SettingsDataStore
import com.aenadgrleey.settings.data.provider.datastore.settingDataStore
import com.aenadgrleey.settings.domain.SettingsProvider
import com.aenadgrleey.settings.domain.model.AppSettings
import com.aenadgrleey.settings.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsProviderImpl @Inject constructor(@AppContext private val context: Context) : SettingsProvider {
    override suspend fun settingsFlow(): Flow<AppSettings> = context.settingDataStore.data.map {
        val theme = when (it[SettingsDataStore.appTheme]) {
            "Dark" -> AppTheme.Dark
            "Light" -> AppTheme.Light
            else -> AppTheme.System
        }
        AppSettings(theme)
    }

    override suspend fun updateAppSettings(appSettings: AppSettings?) {
        context.settingDataStore.edit {
            it[SettingsDataStore.appTheme] = appSettings?.appTheme.toString()
        }
    }
}