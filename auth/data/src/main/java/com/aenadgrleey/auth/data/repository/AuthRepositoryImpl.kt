package com.aenadgrleey.auth.data.repository

import com.aenadgrleey.auth.domain.AuthRepository
import com.aenadgrleey.auth.domain.MutableAuthProvider
import com.aenadgrleey.auth.domain.model.AuthInfo
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl
@Inject constructor(
    private val authProvider: MutableAuthProvider,
) : AuthRepository {
    override suspend fun signIn(token: String) {
        authProvider.updateAuthInfo(
            AuthInfo(
                authToken = token,
                deviceId = UUID.randomUUID().toString()
            )
        )
    }

    override suspend fun signOut() {
        authProvider.clearAuthInfo()
    }
}