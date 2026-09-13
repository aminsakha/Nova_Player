package com.example.novaplayer.features.permission.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PrivacyPolicyDialog(
    onDismiss: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Privacy Policy",
                color = colors.onSurface,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = "This app needs access to your audio files to scan and display music stored on your device. Your music files stay on your device and are not uploaded or shared.",
                color = colors.onSurface.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Close",
                    color = colors.primary
                )
            }
        }
    )
}