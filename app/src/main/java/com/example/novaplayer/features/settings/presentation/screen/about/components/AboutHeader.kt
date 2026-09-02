package com.example.novaplayer.features.settings.presentation.screen.about.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R

@Composable
fun AboutHeader() {

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    primaryColor.copy(alpha = 0.10f)
                )
                .border(
                    width = 1.dp,
                    color = primaryColor.copy(alpha = 0.25f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = primaryColor,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = stringResource(R.string.feel_the_music),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    primaryColor.copy(alpha = 0.12f)
                )
        ) {
            Text(
                text = stringResource(R.string.version),
                modifier = Modifier
                    .padding(
                        horizontal = 14.dp,
                        vertical = 6.dp
                    ),
                color = primaryColor,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )
    }
}