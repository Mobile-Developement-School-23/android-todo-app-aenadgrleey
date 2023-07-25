package com.aenadgrleey.settings.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aenadgrleey.settings.ui.model.UiAction
import com.aenadgrleey.resources.R as CommonR

@Composable
fun SignOutButton(onUiAction: (UiAction) -> Unit) {
    val haptic = LocalHapticFeedback.current
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        onClick = {
            onUiAction(UiAction.OnSignOutButtonClick)
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
            text = "Sign out",
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

@Preview(showBackground = true)
@Composable
fun SignOutButtonPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        SignOutButton {}
    }
}