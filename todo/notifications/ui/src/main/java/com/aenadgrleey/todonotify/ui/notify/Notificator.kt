package com.aenadgrleey.todonotify.ui.notify

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import javax.inject.Inject

abstract class Notificator : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var repository: TodoItemRepository
    protected fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(TodoNotificationChannel.channel)
    }

    protected fun notificationBuilderWithApi(context: Context): Notification.Builder =
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(context, TodoNotificationChannel.id)
        else Notification.Builder(context)

    companion object {
        const val TAG = "NotificationSender"
    }
}