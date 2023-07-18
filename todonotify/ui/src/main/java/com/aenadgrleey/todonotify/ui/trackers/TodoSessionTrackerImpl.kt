package com.aenadgrleey.todonotify.ui.trackers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.aenadgrleey.core.domain.models.TodoItemData
import com.aenadgrleey.tododomain.local.TodoItemsLocalDataSource
import com.aenadgrleey.todonotify.domain.TodoSessionTracker
import com.aenadgrleey.todonotify.ui.notify.NotificationSender
import com.aenadgrleey.todonotify.ui.utils.TodoNotify
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class TodoSessionTrackerImpl : TodoSessionTracker() {

    private val serviceScope = CoroutineScope(Dispatchers.Unconfined)

    @Inject
    lateinit var localDataSource: TodoItemsLocalDataSource

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
        serviceScope.launch {
            localDataSource.getTodoItems(true).collectLatest { it.forEach(::handleTodo) }
        }
    }

    override fun handleTodo(todoItemData: TodoItemData) {
        if (todoItemData.deadline == null) return
        if (todoItemData.deadline!!.time < Calendar.getInstance().time.time) return
//        if (!todoItemData.deadline!!.isToday()) return
        val alarmManager = this.getSystemService(AlarmManager::class.java) as AlarmManager
        val intent = Intent(this, NotificationSender::class.java).apply {
            action = TodoNotify.Action
            putExtra(TodoNotify.TodoItemIdTag, todoItemData.id!!)
        }
        Log.v(TAG, "postponing notification")
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            todoItemData.deadline!!.time,
            PendingIntent.getBroadcast(
                this,
                todoItemData.id!!.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): TodoSessionTracker = this@TodoSessionTrackerImpl
    }

    override fun onBind(intent: Intent): IBinder {
        println("service connected")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        this.stopSelf()
        return false
    }

    override fun onDestroy() {
        serviceScope.cancel()
        super.onDestroy()
    }

    companion object {
        const val dayMilliseconds = 24 * 3600000L
        const val TAG = "TodoSessionTracker"
    }
}