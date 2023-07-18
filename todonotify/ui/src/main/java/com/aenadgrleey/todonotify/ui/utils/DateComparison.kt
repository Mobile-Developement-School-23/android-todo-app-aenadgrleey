package com.aenadgrleey.todonotify.ui.utils

import java.util.Calendar
import java.util.Date

fun Date.isToday(): Boolean {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.DAY_OF_YEAR) == calendar.apply { time = this@isToday }.get(Calendar.DAY_OF_YEAR)
}