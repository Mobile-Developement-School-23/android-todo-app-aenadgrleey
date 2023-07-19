package com.aenadgrleey.todo.refactor.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aenadgrleey.resources.R
import com.aenadgrleey.todo.refactor.ui.model.UiAction


@Composable
fun RefactorScreenTextField(
    error: Boolean,
    text: String,
    onUiAction: (UiAction) -> Unit,
) {
    var value by remember { mutableStateOf(text) }
    val hintText = LocalContext.current.resources.getString(R.string.insertTask)
    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
            onUiAction(UiAction.OnTextChange(it))
        },
        isError = error,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .heightIn(min = 128.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
        ),
        label = {
            Text(
                text = hintText,
                color = MaterialTheme.colorScheme.outline
            )
        },
        shape = RoundedCornerShape(4.dp)
    )
}

@Preview
@Composable
fun TextFieldPreview() {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        RefactorScreenTextField(error = false, text = "aaa", onUiAction = {})
    }
}