package com.aenadgrleey.todo.refactor.ui.composables

import android.content.Context
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.tooling.PreviewActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date

fun launchDateTimePickers(curValue: Date?, context: Context, onSuccess: (Date) -> Unit) {
    if (context !is PreviewActivity) {
        val calendar = Calendar.getInstance()
        if (curValue != null) calendar.time = curValue
        MaterialDatePicker.Builder
            .datePicker()
            .setSelection(
                calendar.timeInMillis +
                        /// crutchhhh
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            ZonedDateTime.now().offset.totalSeconds * 1000
                        } else 0
            )
            .build()
            .apply {
                addOnNegativeButtonClickListener {
                    this.view!!.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                }
                addOnPositiveButtonClickListener { date ->
                    this.view!!.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                    val timePicker = MaterialTimePicker
                        .Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                        .setMinute(calendar.get(Calendar.MINUTE))
                        .build()
                    timePicker.addOnPositiveButtonClickListener {
                        it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                        calendar.time = Date(date)
                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                        calendar.set(Calendar.MINUTE, timePicker.minute)
                        onSuccess(calendar.time)
                    }
                    timePicker.addOnNegativeButtonClickListener {
                        it.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
                    }
                    timePicker.show((activity as AppCompatActivity).supportFragmentManager, "timePicker")
                }
            }
            .show((context as AppCompatActivity).supportFragmentManager, "datePicker")
    }
}