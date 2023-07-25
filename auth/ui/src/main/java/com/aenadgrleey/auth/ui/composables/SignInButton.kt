package com.aenadgrleey.auth.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aenadgrleey.auth.ui.models.UiAction
import com.aenadgrleey.resources.R as CommonR


@Composable
fun AuthScreenSignInButton(modifier: Modifier = Modifier, onUiAction: (UiAction) -> Unit) {
    val haptic = LocalHapticFeedback.current
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        onClick = {
            onUiAction(UiAction.OnSignInButtonClick)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Red.copy(0.3f)
        ),
        contentPadding = PaddingValues(vertical = 0.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(8.dp)
                .size(40.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(CommonR.drawable.yandex_logo),
            contentDescription = null
        )
        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp),
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = LocalContext.current.resources.getString(CommonR.string.signInTitle),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(10.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}