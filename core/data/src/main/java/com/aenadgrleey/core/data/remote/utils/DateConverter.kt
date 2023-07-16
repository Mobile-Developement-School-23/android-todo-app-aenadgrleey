package com.aenadgrleey.core.data.remote.utils

import java.util.Date

/*
Date to Long converter
 */
object DateConverter {
    fun toLong(date: Date) = date.time

    fun toDate(date: Long) = Date(date)
}