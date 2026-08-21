package com.example.novaplayer.features.player.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AlbumArtwork(
    artworkData: ByteArray?,
    modifier: Modifier = Modifier
) {
    val artwork = rememberAlbumArtwork(
        artworkData = artworkData
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant
            ),
        contentAlignment = Alignment.Center
    ) {
        if (artwork != null) {
            Image(
                bitmap = artwork,
                contentDescription = "Album artwork",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_album_placeholder
                ),
                contentDescription = "Default album artwork",
                modifier = Modifier.size(128.dp),
                contentScale = ContentScale.Fit,
                alpha = 0.85f
            )
        }
    }
}

@Composable
private fun rememberAlbumArtwork(
    artworkData: ByteArray?
): ImageBitmap? {
    return produceState<ImageBitmap?>(
        initialValue = null,
        key1 = artworkData
    ) {
        val imageBytes = artworkData

        value = if (
            imageBytes == null ||
            imageBytes.isEmpty()
        ) {
            null
        } else {
            withContext(Dispatchers.Default) {
                runCatching {
                    BitmapFactory.decodeByteArray(
                        imageBytes,
                        0,
                        imageBytes.size
                    )?.asImageBitmap()
                }.getOrNull()
            }
        }
    }.value
}

