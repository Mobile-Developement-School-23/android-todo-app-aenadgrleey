package com.aenadgrleey.auth.domain.model

data class AuthInfo(
    val authToken: String?,
    val deviceId: String?,
)