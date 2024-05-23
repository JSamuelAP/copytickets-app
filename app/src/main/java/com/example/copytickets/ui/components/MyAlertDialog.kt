package com.example.copytickets.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MyAlertDialog(
    title: String,
    text: String,
    show: Boolean,
    onDismiss: () -> Unit
) {
    val isError: Boolean = when (title) {
        "ACEPTADO" -> false
        else -> true
    }
    val bgColor = if (isError) MaterialTheme.colorScheme.errorContainer
    else Color(200, 230, 201)
    val textColor = if (isError) MaterialTheme.colorScheme.onErrorContainer
    else Color(27,94,32)

    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "OK", color = textColor)
                }
            },
            icon = {
                if (isError) Icon(Icons.Outlined.Close, contentDescription = "Error")
                else Icon(Icons.Outlined.CheckCircle, contentDescription = "Ok")
            },
            title = {
                Text(title)
            },
            text = {
                Text(text)
            },
            containerColor = bgColor,
            iconContentColor = textColor,
            titleContentColor = textColor,
            textContentColor = textColor
        )
    }
}

@Preview
@Composable
private fun AlertDialogSuccessPreview() {
    MyAlertDialog(
        title = "ACEPTADO",
        text = "This is a custom alert dialog",
        show = true,
        onDismiss = {}
    )
}

@Preview
@Composable
private fun AlertDialogErrorPreview() {
    MyAlertDialog(
        title = "DUPLICADO",
        text = "This is a custom alert dialog",
        show = true,
        onDismiss = {}
    )
}