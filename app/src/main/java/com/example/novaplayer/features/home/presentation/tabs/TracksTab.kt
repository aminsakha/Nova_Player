package com.example.novaplayer.features.home.presentation.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.novaplayer.features.home.domain.fakeTracks
import com.example.novaplayer.features.home.presentation.screen.component.SongItemCard

@Composable
fun TracksTab(modifier: Modifier = Modifier) {
    Box(Modifier.fillMaxSize().padding(top = 16.dp, start = 8.dp, end = 8.dp)) {
        Column() {
            LazyColumn(Modifier.fillMaxHeight()) {
                items(fakeTracks) {

                    SongItemCard(it)
                }
            }


        }
    }

}