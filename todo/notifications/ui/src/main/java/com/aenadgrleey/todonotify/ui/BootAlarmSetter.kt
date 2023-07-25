package com.aenadgrleey.todonotify.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aenadgrleey.todo.domain.local.TodoItemsLocalDataSource
import com.aenadgrleey.todonotify.domain.TodoNotificationDispatcher
import com.aenadgrleey.todonotify.ui.di.TodoNotificatorComponentProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class BootAlarmSetter : BroadcastReceiver() {
    @Inject
    lateinit var localDataSource: TodoItemsLocalDataSource

    @Inject
    lateinit var notificationDispatcher: TodoNotificationDispatcher
    override fun onReceive(context: Context?, intent: Intent?) {
        context ?: return
        intent ?: return
        intent.action ?: return
        if (intent.action == "android.intent.action.BOOT_COMPLETED"
            || intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            (context as TodoNotificatorComponentProvider).provideNotificatorComponent()
                .inject(this)
            CoroutineScope(Job()).launch(Dispatchers.IO) { localDataSource.getTodoItems().forEach(notificationDispatcher::handleTodo) }
        }
    }
}