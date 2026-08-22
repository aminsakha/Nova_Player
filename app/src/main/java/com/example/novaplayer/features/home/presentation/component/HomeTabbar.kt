package com.example.novaplayer.features.home.presentation.component

import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.novaplayer.R

@Composable
fun HomeTabBar(selectTab: HomeTab, onTabSelected: (HomeTab) -> Unit) {
    PrimaryTabRow(
        selectedTabIndex = selectTab.ordinal
    ) {
        Tab(
            selected = selectTab == HomeTab.PLAYLISTS,
            onClick = { onTabSelected(HomeTab.PLAYLISTS) },
            text = {
                Text(stringResource(R.string.home_tab_playlists))
            }
        )

        Tab(
            selected = selectTab == HomeTab.TRACKS,
            onClick = { onTabSelected(HomeTab.TRACKS) },
            text = {
                Text(stringResource(R.string.home_tab_tracks))
            }
        )

        Tab(
            selected = selectTab == HomeTab.FAVORITES,
            onClick = { onTabSelected(HomeTab.FAVORITES) },
            text = {
                Text(stringResource(R.string.home_tab_favorites))
            }
        )

        Tab(
            selected = selectTab == HomeTab.RECENT,
            onClick = { onTabSelected(HomeTab.RECENT) },
            text = {
                Text(stringResource(R.string.home_tab_recent))
            }
        )
    }

}

enum class HomeTab {
    PLAYLISTS,
    TRACKS,
    FAVORITES,
    RECENT
}