package com.aenadgrleey.todonotify.ui.notify

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.aenadgrleey.tododomain.local.TodoItemsLocalDataSource
import dagger.android.AndroidInjection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import com.aenadgrleey.resources.R as CommonR


class NotificationSender : BroadcastReceiver() {

    @Inject
    lateinit var localDatasource: TodoItemsLocalDataSource

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        Log.v(TAG, "sending notification")
        val todoItemId = intent?.extras?.getString("TodoItemId") ?: return
        GlobalScope.launch(Dispatchers.IO) {
            val todoItem = localDatasource.todoItem(todoItemId) ?: return@launch
            if (todoItem.completed == true) return@launch
            if (((todoItem.deadline?.time ?: 0L) - 100000) > Calendar.getInstance().time.time) return@launch

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val NOTIFICATION_CHANNEL_ID = "com.aenadgrleey.todonotify"
                val notificationManager = NotificationManagerCompat.from(context!!)

                val name = "Notifications"
                val descriptionText = "notify"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
                mChannel.description = descriptionText
                notificationManager.createNotificationChannel(mChannel)


                val notificationBuilder = Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                val notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(CommonR.drawable.check_circle_fill0_wght400_grad0_opsz48)
                    .setContentTitle("Deadline is coming")
                    .setContentText("Todo: ${todoItem.body}")
                    .setChannelId(NOTIFICATION_CHANNEL_ID)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setAutoCancel(true)
                    .setOngoing(false)
                    .build()

                Log.v(TAG, "about to send notification")

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return@launch
                notificationManager.notify(todoItemId.hashCode(), notification)
                Log.v(TAG, "sent notification")
            }
        }
    }

    companion object {
        const val TAG = "NotificationSender"
    }
}