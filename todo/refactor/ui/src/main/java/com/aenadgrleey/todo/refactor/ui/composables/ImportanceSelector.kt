package com.aenadgrleey.todo.refactor.ui.composables

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.aenadgrleey.resources.R.string
import com.aenadgrleey.todo.domain.models.Importance
import com.aenadgrleey.todo.refactor.ui.model.UiAction

@Composable
fun RefactorScreenImportanceSelector(selected: Importance, onUiAction: (UiAction) -> Unit) {
    val cornerRadius = remember { 8.dp }
    val color = remember { Animatable(Color.Red.copy(0.5f)) }
    Column(Modifier.padding(8.dp)) {
        Text(
            text = LocalContext.current.resources.getString(string.importance),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(10.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {

            val items = remember { listOf(Importance.High, Importance.Common, Importance.Low) }

            items.forEachIndexed { index, importance ->
                OutlinedButton(
                    onClick = { onUiAction(UiAction.OnImportanceChange(importance)) },
                    shape = when (index) {
                        0 -> RoundedCornerShape(topStart = cornerRadius, topEnd = 0.dp, bottomStart = cornerRadius, bottomEnd = 0.dp)
                        items.lastIndex -> RoundedCornerShape(topStart = 0.dp, topEnd = cornerRadius, bottomStart = 0.dp, bottomEnd = cornerRadius)
                        else -> RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                    },
                    modifier = when (index) {
                        0 ->
                            Modifier
                                .offset(0.dp, 0.dp)
                                .zIndex(if (importance == selected) 1f else 0f)

                        else ->
                            Modifier
                                .offset((-1 * index).dp, 0.dp)
                                .zIndex(if (importance == selected) 1f else 0f)
                    },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor =
                        if (selected == importance)
                            if (importance == Importance.High) color.value
                            else MaterialTheme.colorScheme.secondaryContainer
                        else Color.Transparent
                    )
                ) {
                    val res = LocalContext.current.resources
                    Text(
                        text = when (importance) {
                            Importance.Low -> res.getString(string.lowImportance)
                            Importance.Common -> res.getString(string.commonImportance)
                            Importance.High -> res.getString(string.highImportance)
                        },
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            var internalState by rememberSaveable { mutableStateOf(selected) }
            LaunchedEffect(selected) {
                if (internalState == selected) {
                    internalState = selected
                    return@LaunchedEffect
                }
                internalState = selected
                if (selected != Importance.High) return@LaunchedEffect
                val initColor = color.value
                val duration = 150
                color.animateTo(initColor.copy(0.8f), animationSpec = tween(duration))
                color.animateTo(initColor.copy(0.5f), animationSpec = tween(duration))
                color.animateTo(initColor.copy(0.8f), animationSpec = tween(duration))
                color.animateTo(initColor, animationSpec = tween(duration))
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview
@Composable
fun ImportancePreview() {
    var selected by remember {
        mutableStateOf(Importance.Common)
    }
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        RefactorScreenImportanceSelector(selected = selected, onUiAction = {})
    }
}