package com.aenadgrleey.settings.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.aenadgrleey.settings.domain.model.AppTheme
import com.aenadgrleey.settings.ui.model.UiAction
import com.aenadgrleey.resources.R as CommonR

@Composable
fun SettingScreenThemeSelector(currentTheme: AppTheme, onUiAction: (UiAction) -> Unit) {
    val themes = remember { listOf(AppTheme.Light, AppTheme.Dark, AppTheme.System) }
    val cornerRadius = remember { 8.dp }
    val haptic = LocalHapticFeedback.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        themes.forEachIndexed { index, theme ->
            OutlinedButton(
                onClick = {
                    onUiAction(UiAction.OnThemeSelect(theme))
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                },
                shape = when (index) {
                    // left outer button
                    0 -> RoundedCornerShape(topStart = cornerRadius, topEnd = 0.dp, bottomStart = cornerRadius, bottomEnd = 0.dp)
                    // right outer button
                    themes.lastIndex -> RoundedCornerShape(topStart = 0.dp, topEnd = cornerRadius, bottomStart = 0.dp, bottomEnd = cornerRadius)
                    // middle button
                    else -> RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                },
                modifier = when (index) {
                    0 ->
                        Modifier
                            .weight(1f)
                            .offset(0.dp, 0.dp)
                            .zIndex(if (currentTheme == theme) 1f else 0f)

                    else ->
                        Modifier
                            .weight(1f)
                            .offset((-1 * index).dp, 0.dp)
                            .zIndex(if (currentTheme == theme) 1f else 0f)
                },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor =
                    if (currentTheme == theme) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                val res = LocalContext.current.resources
                Text(
                    text = when (theme) {
                        AppTheme.Light -> res.getString(CommonR.string.lightTheme)
                        AppTheme.Dark -> res.getString(CommonR.string.darkTheme)
                        AppTheme.System -> res.getString(CommonR.string.systemTheme)
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Preview()
@Composable
fun ThemeSelectorPreview() {
    var current by remember { mutableStateOf(AppTheme.Light) }
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        Column {
            SettingScreenThemeSelector(currentTheme = current) {}
        }
    }
}