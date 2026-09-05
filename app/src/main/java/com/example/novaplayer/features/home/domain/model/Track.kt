package com.example.novaplayer.features.home.domain.model

import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val id: Long=0,
    val title: String="",
    val artist: String="",
    val album: String?="",
    val duration: Long=0L,
    val uri: String="",
    val albumArtUri: String?

)
val fakeTracks = listOf(
    Track(
        id = 1,
        title = "Blinding Lights",
        artist = "The Weeknd",
        album = "After Hours",
        duration = 200000,
        uri = "fake://track/1",
        albumArtUri = "fake://album/1"
    ),
    Track(
        id = 2,
        title = "Save Your Tears",
        artist = "The Weeknd",
        album = "After Hours",
        duration = 215000,
        uri = "fake://track/2",
        albumArtUri = "fake://album/1"
    ),
    Track(
        id = 3,
        title = "Believer",
        artist = "Imagine Dragons",
        album = "Evolve",
        duration = 204000,
        uri = "fake://track/3",
        albumArtUri = "fake://album/2"
    ),
    Track(
        id = 4,
        title = "Thunder",
        artist = "Imagine Dragons",
        album = "Evolve",
        duration = 187000,
        uri = "fake://track/4",
        albumArtUri = "fake://album/2"
    ),
    Track(
        id = 5,
        title = "Shape of You",
        artist = "Ed Sheeran",
        album = "Divide",
        duration = 234000,
        uri = "fake://track/5",
        albumArtUri = "fake://album/3"
    ),
    Track(
        id = 6,
        title = "Perfect",
        artist = "Ed Sheeran",
        album = "Divide",
        duration = 263000,
        uri = "fake://track/6",
        albumArtUri = "fake://album/3"
    ),
    Track(
        id = 7,
        title = "As It Was",
        artist = "Harry Styles",
        album = "Harry's House",
        duration = 167000,
        uri = "fake://track/7",
        albumArtUri = "fake://album/4"
    ),
    Track(
        id = 8,
        title = "Watermelon Sugar",
        artist = "Harry Styles",
        album = "Fine Line",
        duration = 174000,
        uri = "fake://track/8",
        albumArtUri = "fake://album/5"
    ),
    Track(
        id = 9,
        title = "Levitating",
        artist = "Dua Lipa",
        album = "Future Nostalgia",
        duration = 203000,
        uri = "fake://track/9",
        albumArtUri = "fake://album/6"
    ),
    Track(
        id = 10,
        title = "Don't Start Now",
        artist = "Dua Lipa",
        album = "Future Nostalgia",
        duration = 183000,
        uri = "fake://track/10",
        albumArtUri = "fake://album/6"
    ),
    Track(
        id = 11,
        title = "Stay",
        artist = "The Kid LAROI & Justin Bieber",
        album = "F*ck Love 3",
        duration = 141000,
        uri = "fake://track/11",
        albumArtUri = "fake://album/7"
    ),
    Track(
        id = 12,
        title = "Lovely",
        artist = "Billie Eilish & Khalid",
        album = "13 Reasons Why",
        duration = 200000,
        uri = "fake://track/12",
        albumArtUri = "fake://album/8"
    ),
    Track(
        id = 13,
        title = "Happier Than Ever",
        artist = "Billie Eilish",
        album = "Happier Than Ever",
        duration = 298000,
        uri = "fake://track/13",
        albumArtUri = "fake://album/9"
    ),
    Track(
        id = 14,
        title = "Counting Stars",
        artist = "OneRepublic",
        album = "Native",
        duration = 257000,
        uri = "fake://track/14",
        albumArtUri = "fake://album/10"
    ),
    Track(
        id = 15,
        title = "Demons",
        artist = "Imagine Dragons",
        album = "Night Visions",
        duration = 177000,
        uri = "fake://track/15",
        albumArtUri = "fake://album/11"
    ),
    Track(
        id = 16,
        title = "Radioactive",
        artist = "Imagine Dragons",
        album = "Night Visions",
        duration = 187000,
        uri = "fake://track/16",
        albumArtUri = "fake://album/11"
    ),
    Track(
        id = 17,
        title = "Someone Like You",
        artist = "Adele",
        album = "21",
        duration = 285000,
        uri = "fake://track/17",
        albumArtUri = "fake://album/12"
    ),
    Track(
        id = 18,
        title = "Rolling in the Deep",
        artist = "Adele",
        album = "21",
        duration = 228000,
        uri = "fake://track/18",
        albumArtUri = "fake://album/12"
    ),
    Track(
        id = 19,
        title = "Faded",
        artist = "Alan Walker",
        album = "Different World",
        duration = 212000,
        uri = "fake://track/19",
        albumArtUri = "fake://album/13"
    ),
    Track(
        id = 20,
        title = "On My Way",
        artist = "Alan Walker",
        album = "Different World",
        duration = 194000,
        uri = "fake://track/20",
        albumArtUri = "fake://album/13"
    )
)
