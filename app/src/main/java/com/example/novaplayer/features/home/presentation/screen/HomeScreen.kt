package com.example.novaplayer.features.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.novaplayer.features.home.data.HomeTabs
import com.example.novaplayer.features.home.presentation.component.HomeTabBar
import com.example.novaplayer.features.home.presentation.component.MiniPlayerComp
import com.example.novaplayer.features.home.presentation.tabs.FavoritesTab
import com.example.novaplayer.features.home.presentation.tabs.PlayListTab
import com.example.novaplayer.features.home.presentation.tabs.RecentTab
import com.example.novaplayer.features.home.presentation.tabs.TracksTab

@Preview(showSystemUi = true)
@Composable
fun HomeSc() {
    var selectedTab by rememberSaveable {
        mutableStateOf(HomeTabs.TRACKS)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Column(Modifier.padding(8.dp)) {
            Toolbar()
            HomeTabBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }

            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(1f), contentAlignment = Alignment.BottomCenter
            ) {
                when (selectedTab) {
                    HomeTabs.PLAYLISTS -> PlayListTab()
                    HomeTabs.TRACKS -> TracksTab()
                    HomeTabs.FAVORITES -> FavoritesTab()
                    HomeTabs.RECENT -> RecentTab()
                }
                MiniPlayerComp()

            }


        }
    }
}

@Composable
fun Toolbar() {
    Row(Modifier.padding(8.dp)) {
        Text(
            "Novaplayer",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Default.Search, contentDescription = ""
            )

            Icon(
                modifier = Modifier.size(28.dp),

                imageVector = Icons.Default.Menu, contentDescription = ""
            )
        }
    }
}