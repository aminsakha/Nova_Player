package com.example.novaplayer.features.home.presentation.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.novaplayer.features.home.domain.fakeTracks
import com.example.novaplayer.features.home.presentation.screen.component.SongItemCard

@Composable fun TracksTab(modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxSize()){
        Column() {
            fakeTracks.forEach {
                SongItemCard(it)
            }

        }
    }

}