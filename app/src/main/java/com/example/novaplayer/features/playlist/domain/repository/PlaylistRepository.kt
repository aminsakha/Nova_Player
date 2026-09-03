package com.example.novaplayer.features.playlist.domain.repository

import com.example.novaplayer.features.playlist.data.local.dao.PlaylistDao
import com.example.novaplayer.features.playlist.data.local.dao.PlaylistWithCount
import com.example.novaplayer.features.playlist.data.local.entity.PlaylistTrackEntity
import com.example.novaplayer.features.playlist.data.local.entity.playListEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getPlaylists(): Flow<List<playListEntity>>

    fun getPlaylistsWithCount(): Flow<List<PlaylistWithCount>>

    suspend fun addPlaylist(name: String)

    suspend fun deletePlaylist(playlist: playListEntity)

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long, trackCover: String? = null)

    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    fun getTracksForPlaylist(playlistId: Long): Flow<List<Long>>
}

class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {

    override fun getPlaylists(): Flow<List<playListEntity>> {
        return playlistDao.getAllPlaylists()
    }

    override fun getPlaylistsWithCount(): Flow<List<PlaylistWithCount>> {
        return playlistDao.getPlaylistsWithCount()
    }

    override suspend fun addPlaylist(name: String) {
        playlistDao.insertPlaylist(
            playListEntity(
                name = name
            )
        )
    }

    override suspend fun deletePlaylist(playlist: playListEntity) {
        playlistDao.deletePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long, trackCover: String?) {
        playlistDao.insertTrackToPlaylist(
            PlaylistTrackEntity(playlistId, trackId)
        )
        //setting the playlist cover with the first added song's cover
        val playlist = playlistDao.getPlaylistById(playlistId)
        if (playlist != null && playlist.imageUri == null && trackCover != null) {
            playlistDao.updatePlaylist(
                playlist.copy(imageUri = trackCover)
            )
        }
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        playlistDao.removeTrackFromPlaylist(
            PlaylistTrackEntity(playlistId, trackId)
        )
    }

    override fun getTracksForPlaylist(playlistId: Long): Flow<List<Long>> {
        return playlistDao.getTracksForPlaylist(playlistId)
    }
}
