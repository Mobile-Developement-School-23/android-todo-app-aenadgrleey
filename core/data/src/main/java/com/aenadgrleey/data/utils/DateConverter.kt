package com.aenadgrleey.data.utils

import java.util.Date

/*
Date to Long converter
 */
object DateConverter {
    fun toLong(date: Date) = date.time

    fun toDate(date: Long) = Date(date)
}