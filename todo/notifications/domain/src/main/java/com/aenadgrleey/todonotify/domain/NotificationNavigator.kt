package com.aenadgrleey.todonotify.domain

import android.content.Intent

interface NotificationNavigator {
    val intentFromNotificationToActivity: Intent
}