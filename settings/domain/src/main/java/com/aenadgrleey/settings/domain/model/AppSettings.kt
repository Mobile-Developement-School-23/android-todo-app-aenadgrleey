package com.aenadgrleey.settings.domain.model

data class AppSettings(
    val appTheme: AppTheme,
    val notificationPermissionGranted: Boolean,
    val logged: Boolean,
)