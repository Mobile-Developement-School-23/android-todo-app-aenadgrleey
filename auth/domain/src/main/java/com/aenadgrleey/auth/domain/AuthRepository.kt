package com.aenadgrleey.auth.domain

interface AuthRepository {

    suspend fun signIn(token: String)

    suspend fun signOut()
}