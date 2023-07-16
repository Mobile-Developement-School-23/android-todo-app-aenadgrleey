package com.aenadgrleey.settings.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aenadgrleey.resources.R as CommonR

@Composable
fun SignOutButton(onClick: () -> Unit) {
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
    ) {
        Row {
            Image(
                modifier = Modifier
                    .padding(start = 0.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                    .size(30.dp),
                painter = painterResource(CommonR.drawable.yandex_logo),
                contentDescription = null
            )
            Text(
                text = "Sign out",
                modifier = Modifier.align(Alignment.CenterVertically),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}