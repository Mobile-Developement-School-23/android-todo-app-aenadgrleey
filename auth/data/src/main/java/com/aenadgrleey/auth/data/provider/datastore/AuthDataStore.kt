package com.aenadgrleey.auth.data.provider.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object AuthDataStore {
    const val authDataStore = "auth"
    val token = stringPreferencesKey("userToken")
    val deviceId = stringPreferencesKey("deviceId")
}