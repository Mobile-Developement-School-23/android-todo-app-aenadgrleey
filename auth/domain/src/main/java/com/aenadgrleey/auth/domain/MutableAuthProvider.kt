package com.aenadgrleey.auth.domain

import com.aenadgrleey.auth.domain.model.AuthInfo

interface MutableAuthProvider : AuthProvider {

    suspend fun clearAuthInfo()

    suspend fun updateAuthInfo(authInfo: AuthInfo)
}