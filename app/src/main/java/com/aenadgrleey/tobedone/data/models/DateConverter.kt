package com.aenadgrleey.tobedone.data.models

import java.util.Date

class DateConverter {

    companion object {
        fun toLong(date: Date) = date.time

        fun toDate(date: Long) = Date(date)
    }
}