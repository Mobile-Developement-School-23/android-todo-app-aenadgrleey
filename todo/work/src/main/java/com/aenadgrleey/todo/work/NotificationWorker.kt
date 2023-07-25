package com.aenadgrleey.todo.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.aenadgrleey.todo.domain.local.TodoItemsLocalDataSource
import com.aenadgrleey.todonotify.domain.TodoNotificationDispatcher
import javax.inject.Inject
import javax.inject.Provider

class NotificationWorker(
    context: Context,
    params: WorkerParameters,
    private val local: TodoItemsLocalDataSource,
    private val notificationDispatcher: TodoNotificationDispatcher,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        local.getTodoItems().forEach(notificationDispatcher::handleTodo)
        return Result.success()
    }

    class Factory @Inject constructor(
        private val local: Provider<TodoItemsLocalDataSource>,
        private val notificationDispatcher: Provider<TodoNotificationDispatcher>,
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return NotificationWorker(appContext, params, local.get(), notificationDispatcher.get())
        }
    }
}