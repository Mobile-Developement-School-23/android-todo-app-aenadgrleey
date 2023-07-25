package com.aenadgrleey.todo.work

import androidx.work.Constraints
import androidx.work.NetworkType
import java.util.concurrent.TimeUnit

object WorkersConstants {
    const val PERIODIC_SYNC_WORKER = "PERIODIC_SYNC_WORKER"
    const val UPDATE_REMOTE_WORKER = "UPDATE_REMOTE_WORKER"
    const val NOTIFICATION_WORKER = "NOTIFICATION_WORKER"
    val syncWorkerRepeatPeriod = Interval(8, TimeUnit.HOURS)
    val syncWorkerFlexInterval = Interval(15, TimeUnit.MINUTES)
    val notificationWorkerRepeatPeriod = Interval(8, TimeUnit.HOURS)
    val notificationWorkerFlexInterval = Interval(15, TimeUnit.MINUTES)
    val networkConstraint =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    class Interval(val interval: Long, val timeUnit: TimeUnit)
}