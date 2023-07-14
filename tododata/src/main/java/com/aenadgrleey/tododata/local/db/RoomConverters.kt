package com.aenadgrleey.tododata.local.db

import androidx.room.TypeConverter
import com.aenadgrleey.data.utils.DateConverter
import java.util.Date

/*
Room converters for non-primitives
 */
internal class RoomConverters {
    @TypeConverter
    fun dateToLong(date: Date?) = date?.let { DateConverter.toLong(it) }

    @TypeConverter
    fun longToDate(long: Long?) = long?.let { DateConverter.toDate(it) }
}