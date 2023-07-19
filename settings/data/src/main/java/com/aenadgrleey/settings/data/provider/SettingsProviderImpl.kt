package com.aenadgrleey.settings.data.provider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.datastore.preferences.core.edit
import com.aenadgrleey.auth.domain.AuthProvider
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.settings.data.provider.datastore.SettingsDataStore
import com.aenadgrleey.settings.data.provider.datastore.settingDataStore
import com.aenadgrleey.settings.domain.SettingsProvider
import com.aenadgrleey.settings.domain.model.AppSettings
import com.aenadgrleey.settings.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SettingsProviderImpl @Inject constructor(
    @AppContext private val context: Context,
    private val authProvider: AuthProvider,
) : SettingsProvider {
    override suspend fun settingsFlow(): Flow<AppSettings> =
        context.settingDataStore.data.combine(authProvider.authInfoFlow()) { datastore, authInfo ->
            val theme = when (datastore[SettingsDataStore.appTheme]) {
                "Dark" -> AppTheme.Dark
                "Light" -> AppTheme.Light
                else -> AppTheme.System
            }
            val notificationPermissionGranted =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.checkCallingOrSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
                } else true
            AppSettings(theme, notificationPermissionGranted, authInfo.authToken != null)
        }

    override suspend fun updateAppSettings(appSettings: AppSettings?) {
        context.settingDataStore.edit {
            it[SettingsDataStore.appTheme] = appSettings?.appTheme.toString()
        }
    }
}