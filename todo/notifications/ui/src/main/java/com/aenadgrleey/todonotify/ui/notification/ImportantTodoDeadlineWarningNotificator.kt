package com.aenadgrleey.todonotify.ui.notification

import android.Manifest
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.aenadgrleey.resources.R
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todonotify.ui.di.TodoNotificatorComponentProvider
import com.aenadgrleey.todonotify.ui.notification.notificator.TodoNotificator
import com.aenadgrleey.todonotify.ui.utils.TodoNotification
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar

class ImportantTodoDeadlineWarningNotificator : TodoNotificator() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {

        context ?: return
        val todoItemId = intent?.extras?.getString(TodoNotification.todoItemIdTag) ?: return

        val component = (context.applicationContext as TodoNotificatorComponentProvider).provideNotificatorComponent()
        component.inject(this)

        Log.v(TAG, "aaaa")

        GlobalScope.launch(Dispatchers.IO) {
            val todoItem = repository.todoItem(todoItemId)

            if (todoItem.shouldNotify()) {

                notificationManager = NotificationManagerCompat.from(context)
                createNotificationChannel()

                val notification = notificationBuilderWithApi(context).setOngoing(true)
                    .setSmallIcon(R.drawable.tobedone_small_urgent_icon)
                    .setColor(context.getColor(R.color.icon_color))
                    .setContentTitle(context.getText(R.string.urgentTodoWarning))
                    .setContentText(context.getString(R.string.deadlineNotificationText) + "‼️" + "${todoItem!!.body}")
                    .setCategory(Notification.CATEGORY_ALARM)
                    .setContentIntent(pendingIntentFromNavigatorToActivity(context))
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .addAction(createPostponeAction(context, todoItemId))
                    .addAction(createMarkAsDoneAction(context, todoItemId))
                    .build()


                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return@launch
                notificationManager.notify(todoItemId.hashCode() + TodoNotification.warningNotificationIdModifier, notification)
                Log.v(TAG, "sent warning")
            }
        }
    }

    private fun TodoItemData?.shouldNotify(): Boolean {
        if (this == null) return false
        if (this.completed) return false
        if (this.deadline == null) return false
        if (((this.deadline!!.time) - TodoNotification.spread) > Calendar.getInstance().time.time) return false
        return true
    }
}