package com.aenadgrleey.todo.refactor.ui.composables

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aenadgrleey.resources.R.string
import com.aenadgrleey.todo.refactor.ui.model.UiAction
import java.util.Date
import java.util.Locale

@Composable
fun RefactorScreenDeadlineSelector(enabled: Boolean, date: Date?, onUiAction: (UiAction) -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(radius = LocalConfiguration.current.screenWidthDp.dp)
            ) {
                if (enabled) launchDateTimePickers(date, context) {
                    onUiAction(UiAction.OnDeadlineChange(enabled, it))
                }
            }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = LocalContext.current.resources.getString(string.deadline), style = MaterialTheme.typography.titleMedium)
        val dateTimeFormat by remember { mutableStateOf(SimpleDateFormat("HH:mm dd.MM.yy", Locale("eng"))) }
        val valueStr = date?.let { dateTimeFormat.format(it).toString() } ?: context.resources.getString(string.deadlineExample)
        Row(
            Modifier
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

        Switch(checked = enabled, onCheckedChange = { onUiAction(UiAction.OnDeadlineChange(!enabled, date)) })
    }
}

@Preview
@Composable
fun DeadlineSelectorPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        RefactorScreenDeadlineSelector(enabled = false, date = Date(0), onUiAction = {})
    }
}