package com.aenadgrleey.settings.data.provider.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


internal val Context.settingDataStore: DataStore<Preferences>
        by preferencesDataStore(name = SettingsDataStore.settingDataStore)
