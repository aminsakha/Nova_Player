package com.example.novaplayer.features.home.presentation.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.novaplayer.features.home.domain.model.Track

import com.example.novaplayer.features.home.presentation.screen.component.SongItemCard

@Composable
fun TracksTab(
    tracks: List<Track>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 16.dp,
                start = 8.dp,
                end = 8.dp
            )
    ) {
        items(
            items = tracks,
            key = { track -> track.id }
        ) { track ->
            SongItemCard(track)
        }
    }
}