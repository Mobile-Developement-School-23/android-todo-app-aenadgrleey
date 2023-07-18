package com.aenadgrleey.todo.refactor.ui.composables

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.PreviewActivity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aenadgrleey.resources.R.string
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun RefactorScreenDeadlineSelector(enabled: Boolean, value: Date?, onDateChange: (Date) -> Unit, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = LocalContext.current.resources.getString(string.deadline), style = MaterialTheme.typography.titleMedium)
        val activity = LocalContext.current
        val dateTimeFormat by remember { mutableStateOf(SimpleDateFormat("HH:mm  // dd.MM.yy", Locale("eng"))) }
        val valueStr = value?.let { dateTimeFormat.format(it).toString() } ?: activity.resources.getString(string.deadlineExample)
        val interactionSource = remember { MutableInteractionSource() }
        Row(
            Modifier
                .clickable(interactionSource, indication = null) {
                    if (activity !is PreviewActivity && enabled) {
                        val calendar = Calendar.getInstance()
                        if (value != null) calendar.time = value
                        MaterialDatePicker.Builder
                            .datePicker()
                            .setSelection(calendar.timeInMillis)
                            .build()
                            .apply {
                                addOnPositiveButtonClickListener { date ->
                                    val timePicker = MaterialTimePicker
                                        .Builder()
                                        .setTimeFormat(TimeFormat.CLOCK_24H)
                                        .setInputMode(INPUT_MODE_CLOCK)
                                        .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                                        .setMinute(calendar.get(Calendar.MINUTE))
                                        .build()
                                    timePicker.addOnPositiveButtonClickListener {
                                        calendar.time.time = date
                                        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                                        calendar.set(Calendar.MINUTE, timePicker.minute)
                                        onDateChange(calendar.time)
                                    }
                                    timePicker.show((activity as AppCompatActivity).supportFragmentManager, "timePicker")
                                }
                            }
                            .show((activity as AppCompatActivity).supportFragmentManager, "datePicker")
                    }
                }
                .padding(horizontal = 16.dp)
                .weight(1f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = valueStr, style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                textDecoration = if (!enabled) TextDecoration.LineThrough else TextDecoration.None,
                color = if (!enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
            )
        }

        Switch(checked = enabled, onCheckedChange = { onToggle(!enabled) })
    }
}

@Preview
@Composable
fun DeadlineSelectorPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        RefactorScreenDeadlineSelector(enabled = false, value = Date(0), onDateChange = {}) {}
    }
}