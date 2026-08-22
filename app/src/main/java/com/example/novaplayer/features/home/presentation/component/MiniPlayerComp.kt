package com.example.novaplayer.features.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R
import com.example.novaplayer.core.ui.theme.IconLarge
import com.example.novaplayer.core.ui.theme.Space4

@Composable
fun MiniPlayerComp(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(4.dp), shape = CircleShape
        ) {
            Row(
                Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(
                    Space4
                ), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier.clip(CircleShape)
                )
                Column() {
                    Text(
                        "MusicName",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "singer name",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.SkipPrevious,
                        contentDescription = ""
                    )
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "",
                        modifier = Modifier.size(IconLarge)
                    )
                    Icon(
                        imageVector = Icons.Outlined.SkipNext,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}