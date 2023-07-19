package com.aenadgrleey.todo.refactor.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aenadgrleey.resources.R
import com.aenadgrleey.todo.refactor.ui.model.UiAction
import com.aenadgrleey.todo.refactor.ui.utils.Res

@Composable
fun RefactorScreenDeleteButton(onUiAction: (UiAction) -> Unit) {
    FilledTonalButton(
        onClick = { onUiAction(UiAction.OnDeleteRequest) },
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                //todo
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = Res().getString(R.string.deleteTask),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(10.dp),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}