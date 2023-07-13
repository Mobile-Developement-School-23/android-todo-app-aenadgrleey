package com.aenadgrleey.auth.data.provider.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


internal val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = AuthDataStore.authDataStore)
