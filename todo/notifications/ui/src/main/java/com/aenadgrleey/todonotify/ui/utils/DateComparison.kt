package com.aenadgrleey.todonotify.ui.utils

import java.util.Calendar
import java.util.Date

fun Date.withinNext24Hours(): Boolean {
    val now = Calendar.getInstance().time
    val oneDayInMillis = 24 * 60 * 60 * 1000
    return this.time > now.time && this.time - now.time < oneDayInMillis
}