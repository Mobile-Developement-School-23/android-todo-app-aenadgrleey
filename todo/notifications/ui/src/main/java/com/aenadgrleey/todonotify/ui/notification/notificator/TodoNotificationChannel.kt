package com.aenadgrleey.todonotify.ui.notification.notificator

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

object TodoNotificationChannel {
    const val id = "com.aenadgrleey.todonotify"
    const val name = "Todo notifications"
    const val importance = NotificationManager.IMPORTANCE_DEFAULT

    @RequiresApi(Build.VERSION_CODES.O)
    val channel = NotificationChannel(id, name, importance).apply {
        setShowBadge(true)
        enableVibration(true)
        enableLights(true)
    }
}