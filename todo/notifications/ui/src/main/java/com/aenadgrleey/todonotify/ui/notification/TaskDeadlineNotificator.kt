package com.aenadgrleey.todonotify.ui.notification

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todonotify.ui.di.NotificatorComponentProvider
import com.aenadgrleey.todonotify.ui.notification.notificator.Notificator
import com.aenadgrleey.todonotify.ui.utils.TodoNotification
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import com.aenadgrleey.resources.R as CommonR


class TaskDeadlineNotificator : Notificator() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {

        context ?: return
        val todoItemId = intent?.extras?.getString("TodoItemId") ?: return

        val component = (context.applicationContext as NotificatorComponentProvider).provideNotificatorComponent()
        component.inject(this)

        GlobalScope.launch(Dispatchers.IO) {
            val todoItem = repository.todoItem(todoItemId)

            if (todoItem.shouldNotify()) {

                notificationManager = NotificationManagerCompat.from(context)
                createNotificationChannel()

                val importanceMarker = when (todoItem!!.importance) {
                    Importance.Low -> "⬇️"
                    Importance.High -> "‼️"
                    else -> ""
                }


                val notification = notificationBuilderWithApi(context).setOngoing(true)
                    .setSmallIcon(CommonR.drawable.tobedone_small_icon)
                    .setColor(context.getColor(CommonR.color.icon_color))
                    .setContentTitle(context.getText(CommonR.string.deadlineNotificationTitle))
                    .setContentText(context.getString(CommonR.string.deadlineNotificationText) + importanceMarker + "${todoItem.body}")
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .setContentIntent(pendingIntentFromNavigatorToActivity(context))
                    .build()


                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return@launch
                notificationManager.notify(todoItemId.hashCode(), notification)
                Log.v(TAG, "sent notification")
            }
        }
    }

    private fun TodoItemData?.shouldNotify(): Boolean {
        if (this == null) return false
        if (this.completed == true) return false
        if (((this.deadline?.time ?: 0L) - TodoNotification.spread) > Calendar.getInstance().time.time) return false
        return true
    }
}