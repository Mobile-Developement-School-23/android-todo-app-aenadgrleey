package com.aenadgrleey.todonotify.ui.notification.notificator

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import com.aenadgrleey.todonotify.domain.NotificationNavigator
import javax.inject.Inject

abstract class Notificator : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var repository: TodoItemRepository

    @Inject
    lateinit var navigator: NotificationNavigator

    protected fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(TodoNotificationChannel.channel)
    }

    protected fun notificationBuilderWithApi(context: Context): Notification.Builder =
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(context, TodoNotificationChannel.id)
        else Notification.Builder(context)

    fun pendingIntentFromNavigatorToActivity(context: Context) = PendingIntent.getActivity(
        context,
        0,
        navigator.intentFromNotificationToActivity,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    companion object {
        const val TAG = "NotificationSender"
    }
}