package com.aenadgrleey.todo.refactor.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color

@Composable
fun BlinkingColor(color: Animatable<Color, AnimationVector4D>) {
    LaunchedEffect(Unit) {
        val initColor = color.value
        val duration = 150
        color.animateTo(initColor.copy(0.8f), animationSpec = tween(duration))
        color.animateTo(initColor.copy(0.5f), animationSpec = tween(duration))
        color.animateTo(initColor.copy(0.8f), animationSpec = tween(duration))
        color.animateTo(initColor, animationSpec = tween(duration))
    }
}