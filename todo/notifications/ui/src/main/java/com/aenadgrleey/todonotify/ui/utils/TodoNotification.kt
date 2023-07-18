package com.aenadgrleey.todonotify.ui.utils

object TodoNotification {
    const val NotificationAction = "android.aenadgrleey.SEND_TODO" //used in module manifest
    const val WarningAction = "android.aenadgrleey.WARN_ABOUT_IMPORTANT" //used in module manifest
    const val TodoItemIdTag = "TodoItemId"
    const val WarningNotificationIdModifier = 1000
    const val Spread = 60 * 10000 // one minute
}