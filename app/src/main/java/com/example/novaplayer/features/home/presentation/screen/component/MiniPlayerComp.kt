package com.example.novaplayer.features.home.presentation.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R
import com.example.novaplayer.core.ui.theme.AvatarLarge
import com.example.novaplayer.core.ui.theme.IconLarge


@Composable
fun MiniPlayerComp(
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = CircleShape,
        border = BorderStroke(
            width = 0.7.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                )
            )
        )
    ) {
        Row(
            modifier = Modifier

                .fillMaxWidth()
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Image(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onSurface, shape = CircleShape)
                    .size(AvatarLarge)
                    .clip(CircleShape),
                painter = painterResource(R.drawable.ic_album_placeholder),
                contentDescription = ""
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Music Name",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = "Singer Name",
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.SkipPrevious,
                    contentDescription = "Previous"
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(IconLarge)
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Outlined.SkipNext,
                    contentDescription = "Next"
                )
            }
        }
    }
}