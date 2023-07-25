package com.aenadgrleey.todonotify.ui.utils

object TodoNotification {
    const val notificationAction = "android.aenadgrleey.SEND_TODO" //used in module manifest
    const val warningAction = "android.aenadgrleey.WARN_ABOUT_IMPORTANT" //used in module manifest
    const val launchAppFromNotificationAction = "android.aenadgrleey.START_FROM_NOTIFICATION" //used in app manifest
    const val marksTodoAsDoneNotificationAction = "android.aenadgrleey.MARK_TODO_AS_DONE"
    const val postponeTodoNotificationAction = "android.aenadgrleey.POSTPONE_TODO"
    const val todoItemIdTag = "TodoItemId"
    const val warningNotificationIdModifier = 1000
    const val spread = 60 * 10000 // one minute
    const val hourMilliseconds = 60 * 60 * 1000
    const val warningNotificationTimeBeforeDeadline = 12 * hourMilliseconds
    const val postponingInterval = 23 * hourMilliseconds
}