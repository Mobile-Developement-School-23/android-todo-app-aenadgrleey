package com.aenadgrleey.todonotify.ui.notification.notificator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.aenadgrleey.todo.domain.repository.TodoItemRepository
import com.aenadgrleey.todonotify.ui.di.TodoNotificationActionReceiverComponentProvider
import com.aenadgrleey.todonotify.ui.utils.TodoNotification
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

class TodoNotificationActionReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: TodoItemRepository

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return
        val todoItemId = intent.extras?.getString(TodoNotification.todoItemIdTag) ?: return

        GlobalScope.launch {

            val component = (context.applicationContext as TodoNotificationActionReceiverComponentProvider).provideTodoNotificationActionReceiverComponent()
            component.inject(this@TodoNotificationActionReceiver)
            val todoItem = repository.todoItem(todoItemId) ?: return@launch
            val notificationManager = NotificationManagerCompat.from(context)
            when (intent.action) {

                (TodoNotification.marksTodoAsDoneNotificationAction) -> {
                    repository.addTodoItem(todoItem.copy(completed = true))
                    // cancel main notification
                    notificationManager.cancel(todoItemId.hashCode())
                    // cancel warning
                    notificationManager.cancel(todoItemId.hashCode() + TodoNotification.warningNotificationIdModifier)
                }

                (TodoNotification.postponeTodoNotificationAction) -> {
                    val previousDate = todoItem.deadline ?: return@launch
                    val newDate = Date(previousDate.time + TodoNotification.postponingInterval)
                    repository.addTodoItem(todoItem.copy(deadline = newDate))
                    // cancel main notification
                    notificationManager.cancel(todoItemId.hashCode())
                }

                else -> throw IllegalArgumentException("Received unknown action")
            }
        }
    }
}