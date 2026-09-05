package com.example.novaplayer.features.home.data.datasource

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.example.novaplayer.features.home.data.model.TrackDto
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class MediaStoreAudioDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalAudioDataSource {
    override suspend fun getTrack(uri: String): TrackDto? {
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )

        val selection = "${MediaStore.Audio.Media._ID} = ?"

        val id = ContentUris.parseId(Uri.parse(uri))

        context.contentResolver.query(
            collection,
            projection,
            selection,
            arrayOf(id.toString()),
            null
        )?.use { cursor ->

            if (cursor.moveToFirst()) {

                val trackId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Audio.Media._ID
                    )
                )

                val albumId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(
                        MediaStore.Audio.Media.ALBUM_ID
                    )
                )

                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                )

                return TrackDto(
                    id = trackId,
                    uri = ContentUris.withAppendedId(
                        collection,
                        trackId
                    ).toString(),
                    title = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Audio.Media.TITLE
                        )
                    ) ?: "Unknown",
                    artist = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Audio.Media.ARTIST
                        )
                    ) ?: "Unknown Artist",
                    album = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Audio.Media.ALBUM
                        )
                    ) ?: "Unknown Album",
                    duration = cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Audio.Media.DURATION
                        )
                    ),
                    albumArtUri = albumArtUri.toString()
                )
            }
        }

        return null
    }

    override suspend fun getAllTracks(): List<TrackDto> {
        val tracks = mutableListOf<TrackDto>()

        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DURATION
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        context.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media._ID
            )

            val titleColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media.TITLE
            )

            val artistColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media.ARTIST
            )

            val albumColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media.ALBUM
            )

            val albumIdColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media.ALBUM_ID
            )

            val durationColumn = cursor.getColumnIndexOrThrow(
                MediaStore.Audio.Media.DURATION
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)

                val uri = ContentUris.withAppendedId(
                    collection,
                    id
                )

                val albumId = cursor.getLong(albumIdColumn)

                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                )

                tracks += TrackDto(
                    id = id,
                    title = cursor.getString(titleColumn) ?: "Unknown",
                    artist = cursor.getString(artistColumn) ?: "Unknown Artist",
                    album = cursor.getString(albumColumn) ?: "Unknown Album",
                    duration = cursor.getLong(durationColumn),
                    uri = uri.toString(),
                    albumArtUri = albumArtUri.toString()
                )
            }
        }

        return tracks
    }
}