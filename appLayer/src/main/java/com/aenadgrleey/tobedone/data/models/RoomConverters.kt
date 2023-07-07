package com.aenadgrleey.tobedone.data.models

import androidx.room.TypeConverter
import java.util.Date

class RoomConverters {
    @TypeConverter
    fun dateToLong(date: Date?) = date?.let { DateConverter.toLong(it) }

    @TypeConverter
    fun longToDate(long: Long?) = long?.let { DateConverter.toDate(it) }
}