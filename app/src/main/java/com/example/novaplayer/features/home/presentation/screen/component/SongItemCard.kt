package com.example.novaplayer.features.home.presentation.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R
import com.example.novaplayer.core.ui.theme.AvatarLarge
import com.example.novaplayer.core.ui.theme.Space1
import com.example.novaplayer.core.ui.theme.Space2
import com.example.novaplayer.core.ui.theme.Space4
import com.example.novaplayer.features.home.domain.Track


@Composable
fun SongItemCard(track: Track) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(Space4)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .border(1.dp, color = Color.Black, shape = RoundedCornerShape(Space2))
                    .size(AvatarLarge)
                    .clip(RoundedCornerShape(Space2)),
                painter = painterResource(R.drawable.ic_album_placeholder),
                contentDescription = ""
            )
            Column(
                Modifier.padding(start = Space4),
                verticalArrangement = Arrangement.spacedBy(Space1)
            ) {
                Text(
                    track.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    track.artist, color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.5f
                    ), style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.weight(1f))
            Box(modifier = Modifier.size(20.dp))
            Icon(
                imageVector = Icons.Default.MoreVert, contentDescription = ""
            )
        }
    }
}