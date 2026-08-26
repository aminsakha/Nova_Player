package com.example.novaplayer.features.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.novaplayer.R
import com.example.novaplayer.core.ui.theme.Space2
import com.example.novaplayer.features.home.domain.model.HomeTabs
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.presentation.contract.HomeContract
import com.example.novaplayer.features.home.presentation.contract.LoadingState
import com.example.novaplayer.features.home.presentation.permission.AudioPermissionHandler
import com.example.novaplayer.features.home.presentation.screen.component.HomeTabBar
import com.example.novaplayer.features.home.presentation.screen.component.MiniPlayerComp
import com.example.novaplayer.features.home.presentation.screen.component.Toolbar
import com.example.novaplayer.features.home.presentation.tabs.FavoritesTab
import com.example.novaplayer.features.home.presentation.tabs.PlayListTab
import com.example.novaplayer.features.home.presentation.tabs.RecentTab
import com.example.novaplayer.features.home.presentation.tabs.TracksTab
import com.example.novaplayer.features.home.presentation.viewmodel.HomeViewModel
import com.example.novaplayer.features.settings.presentation.screen.SettingsPanel


@Composable
fun HomeSc(viewModel: HomeViewModel = hiltViewModel(),onTrackClick:(Track)->Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable {
        mutableStateOf(HomeTabs.TRACKS)
    }
    var isSettingsOpen by remember {
        mutableStateOf(false)
    }

    AudioPermissionHandler(
        onPermissionGranted = {
            viewModel.onAction(
                HomeContract.UiAction.GetTracks
            )
        }
    )


    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Toolbar(
                onSettingsClick = {
                    isSettingsOpen = true
                }
            )
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
                Box(Modifier.fillMaxSize()) {
                    when (selectedTab) {
                        HomeTabs.PLAYLISTS -> PlayListTab()
                        HomeTabs.TRACKS -> {
                            when (uiState.loadingState) {
                                LoadingState.IDLE,
                                LoadingState.LOADING -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = stringResource(R.string.loading),
                                                color = MaterialTheme.colorScheme.primary,
                                                style = MaterialTheme.typography.bodyMedium
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            LinearProgressIndicator(
                                                modifier = Modifier.width(120.dp),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }

                                LoadingState.SUCCESS -> {
                                    TracksTab(tracks = uiState.tracks){
                                        onTrackClick(it)
                                    }
                                }

                                LoadingState.ERROR -> {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.error_loading_tracks),
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                        HomeTabs.FAVORITES -> FavoritesTab()
                        HomeTabs.RECENT -> RecentTab()
                    }
                }

            }
        }
    }
    SettingsPanel(
        visible = isSettingsOpen,
        onClose = {
            isSettingsOpen = false
        }
    )
}

