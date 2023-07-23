package com.aenadgrleey.todo.data.local.utils

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toLong(date: Date?) = date?.time

    @TypeConverter
    fun toDate(date: Long?) = date?.let { Date(it) }
}