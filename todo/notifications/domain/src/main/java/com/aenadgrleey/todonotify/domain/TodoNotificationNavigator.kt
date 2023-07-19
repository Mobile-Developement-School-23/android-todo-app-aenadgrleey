package com.aenadgrleey.todonotify.domain

import android.content.Intent

interface TodoNotificationNavigator {
    val intentFromNotificationToActivity: Intent
}