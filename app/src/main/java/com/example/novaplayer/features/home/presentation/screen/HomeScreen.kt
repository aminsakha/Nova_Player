package com.example.novaplayer.features.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.novaplayer.core.ui.theme.Space2
import com.example.novaplayer.features.home.domain.model.HomeTabs
import com.example.novaplayer.features.home.presentation.screen.component.HomeTabBar
import com.example.novaplayer.features.home.presentation.screen.component.MiniPlayerComp
import com.example.novaplayer.features.home.presentation.screen.component.Toolbar
import com.example.novaplayer.features.home.presentation.tabs.FavoritesTab
import com.example.novaplayer.features.home.presentation.tabs.PlayListTab
import com.example.novaplayer.features.home.presentation.tabs.RecentTab
import com.example.novaplayer.features.home.presentation.tabs.TracksTab


@Composable
fun HomeSc() {

    var selectedTab by rememberSaveable {
        mutableStateOf(HomeTabs.TRACKS)
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Toolbar()
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
            ) {
                MiniPlayerComp()
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Space2)
            ) {

                HomeTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
                Box(Modifier.fillMaxSize()){
                    when (selectedTab) {
                        HomeTabs.PLAYLISTS -> PlayListTab()
                        HomeTabs.TRACKS -> TracksTab()
                        HomeTabs.FAVORITES -> FavoritesTab()
                        HomeTabs.RECENT -> RecentTab()
                    }
                }

            }
        }
    }
}

