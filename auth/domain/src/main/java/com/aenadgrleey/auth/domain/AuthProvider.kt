package com.aenadgrleey.auth.domain

import com.aenadgrleey.auth.domain.model.AuthInfo
import kotlinx.coroutines.flow.Flow

interface AuthProvider {
    fun authInfoFlow(): Flow<AuthInfo>

    suspend fun authInfo(): AuthInfo
}