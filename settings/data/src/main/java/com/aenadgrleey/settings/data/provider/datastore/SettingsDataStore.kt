package com.aenadgrleey.settings.data.provider.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsDataStore {
    const val settingDataStore = "settings"
    val appTheme = stringPreferencesKey("appTheme")
}