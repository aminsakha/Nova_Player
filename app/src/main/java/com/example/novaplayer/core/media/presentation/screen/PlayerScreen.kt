package com.example.novaplayer.core.media.presentation.screen

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PlayerScreen(
    songUri: String?,
    modifier: Modifier = Modifier
) {
    val albumArtwork = rememberEmbeddedAlbumArtwork(
        songUri = songUri
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlbumArtwork(
            artwork = albumArtwork,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        )
    }
}

@Composable
private fun AlbumArtwork(
    artwork: ImageBitmap?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(320.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
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
private fun rememberEmbeddedAlbumArtwork(
    songUri: String?
): ImageBitmap? {
    val context = LocalContext.current

    return produceState<ImageBitmap?>(
        initialValue = null,
        key1 = songUri
    ) {
        value = if (songUri.isNullOrBlank()) {
            null
        } else {
            withContext(Dispatchers.IO) {
                val retriever = MediaMetadataRetriever()

                try {
                    retriever.setDataSource(
                        context,
                        Uri.parse(songUri)
                    )

                    retriever.embeddedPicture?.let { imageBytes ->
                        BitmapFactory.decodeByteArray(
                            imageBytes,
                            0,
                            imageBytes.size
                        )?.asImageBitmap()
                    }
                } catch (exception: Exception) {
                    null
                } finally {
                    retriever.release()
                }
            }
        }
    }.value
}