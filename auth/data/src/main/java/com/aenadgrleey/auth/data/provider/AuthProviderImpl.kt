package com.aenadgrleey.auth.data.provider

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.aenadgrleey.auth.BuildConfig
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
        val tokenType = if (BuildConfig.DEBUG) "Bearer " else "OAuth "
        val token = if (BuildConfig.DEBUG) "durry" else it[AuthDataStore.token]
        val deviceId = if (BuildConfig.DEBUG) "debug device" else it[AuthDataStore.deviceId]
        AuthInfo(
            if (token != null) tokenType + token else null,
            if (deviceId != null) "${Build.BRAND} ${Build.MODEL} ($deviceId)" else null
        )
    }

    override suspend fun authInfo() = authInfoFlow().first()

    override suspend fun clearAuthInfo() {
        context.authDataStore.edit {
            it.clear()
        }
    }

    override suspend fun updateAuthInfo(authInfo: AuthInfo) {
        context.authDataStore.edit { settings ->
            try {
                authInfo.authToken!!.also { settings[AuthDataStore.token] = it }
                authInfo.deviceId!!.also { settings[AuthDataStore.deviceId] = it }
            } catch (exception: NullPointerException) {
                Log.e("Auth", "Problems with authorization, clearing datastore")
                settings.clear()
            }
        }
    }
}