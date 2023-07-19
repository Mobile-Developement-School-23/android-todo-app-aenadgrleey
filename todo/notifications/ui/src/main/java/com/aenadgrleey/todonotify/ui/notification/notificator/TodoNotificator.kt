package com.aenadgrleey.todonotify.ui.notification.notificator

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import com.aenadgrleey.todonotify.domain.TodoNotificationNavigator
import com.aenadgrleey.todonotify.ui.utils.TodoNotification
import javax.inject.Inject
import com.aenadgrleey.resources.R as CommonR

abstract class TodoNotificator : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var repository: TodoItemRepository

    @Inject
    lateinit var navigator: TodoNotificationNavigator

    protected fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) notificationManager.createNotificationChannel(TodoNotificationChannel.channel)
    }

    protected fun notificationBuilderWithApi(context: Context): NotificationCompat.Builder =
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) NotificationCompat.Builder(context, TodoNotificationChannel.id)
        else NotificationCompat.Builder(context)

    fun pendingIntentFromNavigatorToActivity(context: Context) = PendingIntent.getActivity(
        context,
        0,
        navigator.intentFromNotificationToActivity,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun createPostponeAction(context: Context, todoItemId: String): NotificationCompat.Action {
        val intent = Intent(context, TodoNotificationActionReceiver::class.java).apply {
            action = TodoNotification.postponeTodoNotificationAction
            putExtra(TodoNotification.todoItemIdTag, todoItemId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action(CommonR.drawable.round_more_time_24, context.getText(CommonR.string.postponeAction), pendingIntent)
    }

    fun createMarkAsDoneAction(context: Context, todoItemId: String): NotificationCompat.Action {
        val intent =
            Intent(context, TodoNotificationActionReceiver::class.java)
                .apply {
                    action = TodoNotification.marksTodoAsDoneNotificationAction
                    putExtra(TodoNotification.todoItemIdTag, todoItemId)
                }

        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Action(CommonR.drawable.round_done_24, context.getString(CommonR.string.markAsDoneAction), pendingIntent)
    }

    companion object {
        const val TAG = "NotificationSender"
    }
}