package com.aenadgrleey.auth.data.provider

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.aenadgrleey.auth.data.provider.datastore.AuthDataStore
import com.aenadgrleey.auth.data.provider.datastore.authDataStore
import com.aenadgrleey.auth.domain.MutableAuthProvider
import com.aenadgrleey.auth.domain.model.AuthInfo
import com.aenadgrleey.core.di.AppContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthProviderImpl
@Inject constructor(@AppContext private val context: Context) : MutableAuthProvider {
    override fun authInfoFlow() = context.authDataStore.data.map {
        AuthInfo(
            it[AuthDataStore.token],
            it[AuthDataStore.deviceId]
        )
    }

    override suspend fun authInfo() = context.authDataStore.data.map {
        AuthInfo(
            it[AuthDataStore.token],
            it[AuthDataStore.deviceId]
        )
    }.first()

    override suspend fun clearAuthInfo() {
        context.authDataStore.edit {
            it.clear()
        }
    }

    override suspend fun updateAuthInfo(authInfo: AuthInfo) {
        context.authDataStore.edit { settings ->
            settings[AuthDataStore.token] = authInfo.authToken!!
            settings[AuthDataStore.deviceId] = authInfo.deviceId!!
        }
    }
}