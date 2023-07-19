package com.aenadgrleey.todo.refactor.ui.composables

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aenadgrleey.resources.R
import com.aenadgrleey.todo.refactor.ui.model.UiAction


@Composable
fun RefactorScreenToolbar(
    dependentScrollState: ScrollState,
    completeness: Boolean,
    onUiAction: (UiAction) -> Unit,
) {
    val elevation by animateIntAsState(targetValue = if (dependentScrollState.value != 0) 10 else 0, label = "elevation")
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surface),
        tonalElevation = elevation.dp
    ) {
        Row {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 8.dp),
                onClick = { onUiAction(UiAction.OnExitRequest) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    modifier = Modifier
                        .size(36.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
            val appName = LocalContext.current.resources.getText(R.string.app_name).toString()
            Text(
                text = appName,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
                    .padding(horizontal = 10.dp),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            val completeColor = Color(LocalContext.current.getColor(R.color.green))
            IconButton(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = { onUiAction(UiAction.OnCompletenessChange(!completeness)) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_check_circle_outline_24),
                    modifier = Modifier
                        .size(36.dp),
                    tint = if (completeness) completeColor else MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
                onClick = { onUiAction(UiAction.OnSaveRequest) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_save_24),
                    modifier = Modifier
                        .size(36.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun ToolbarPreview() = RefactorScreenToolbar(rememberScrollState(), true) {}
