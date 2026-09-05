package com.example.novaplayer.features.playlist.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.novaplayer.features.playlist.data.local.entity.PlaylistTrackEntity
import com.example.novaplayer.features.playlist.data.local.entity.playListEntity
import kotlinx.coroutines.flow.Flow

data class PlaylistWithCount(
    val id: Long,
    val name: String,
    val imageUri: String?,
    val trackCount: Int
)

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<playListEntity>>

    @Query("""
        SELECT p.id, p.name, p.imageUri, COUNT(pt.trackId) as trackCount 
        FROM playlists p 
        LEFT JOIN playlist_tracks pt ON p.id = pt.playlistId 
        GROUP BY p.id
    """)
    fun getPlaylistsWithCount(): Flow<List<PlaylistWithCount>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(
        playlistId: Long
    ): playListEntity?

    @Insert
    suspend fun insertPlaylist(
        playlist: playListEntity
    ): Long

    @Update
    suspend fun updatePlaylist(
        playlist: playListEntity
    )

    @Delete
    suspend fun deletePlaylist(
        playlist: playListEntity
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackToPlaylist(
        playlistTrack: PlaylistTrackEntity
    )

    @Delete
    suspend fun removeTrackFromPlaylist(
        playlistTrack: PlaylistTrackEntity
    )

    @Query("SELECT trackId FROM playlist_tracks WHERE playlistId = :playlistId")
    fun getTracksForPlaylist(
        playlistId: Long
    ): Flow<List<Long>>
}
