package com.aenadgrleey.todonotify.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.aenadgrleey.core.di.AppContext
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.domain.models.TodoItemData
import com.aenadgrleey.todonotify.domain.TodoNotificationDispatcher
import com.aenadgrleey.todonotify.ui.notify.ImportantTaskDeadlineWarningNotificator
import com.aenadgrleey.todonotify.ui.notify.TaskDeadlineNotificator
import com.aenadgrleey.todonotify.ui.utils.TodoNotification
import com.aenadgrleey.todonotify.ui.utils.withinNext24Hours
import java.util.Calendar
import javax.inject.Inject

class TodoNotificationDispatcherImpl @Inject constructor(
    @AppContext private val context: Context,
) : TodoNotificationDispatcher {

    override fun handleTodo(todoItemData: TodoItemData) {
        if (todoItemData.deadline == null) return
        if (todoItemData.deadline!!.time < Calendar.getInstance().time.time) return
        if (!todoItemData.deadline!!.withinNext24Hours()) return
        val alarmManager = context.getSystemService(AlarmManager::class.java) as AlarmManager
        val notificationIntent = Intent(context, TaskDeadlineNotificator::class.java).apply {
            action = TodoNotification.NotificationAction
            putExtra(TodoNotification.TodoItemIdTag, todoItemData.id!!)
        }
        Log.v(TAG, "postponed")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            todoItemData.deadline!!.time,
            PendingIntent.getBroadcast(
                context,
                todoItemData.id!!.hashCode(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
        if (todoItemData.importance == Importance.High) {
            val warningIntent = Intent(context, ImportantTaskDeadlineWarningNotificator::class.java).apply {
                action = TodoNotification.WarningAction
                putExtra(TodoNotification.TodoItemIdTag, todoItemData.id!!)
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                todoItemData.deadline!!.time + hourMilliseconds,
                PendingIntent.getBroadcast(
                    context,
                    todoItemData.id!!.hashCode() + TodoNotification.WarningNotificationIdModifier,
                    warningIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    companion object {
        const val dayMilliseconds = 24 * 60 * 60 * 1000
        const val hourMilliseconds = 60 * 60 * 1000
        const val TAG = "TodoNotificationDispatcher"
    }
}