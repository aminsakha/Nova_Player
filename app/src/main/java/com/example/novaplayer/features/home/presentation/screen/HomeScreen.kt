package com.example.novaplayer.features.home.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.novaplayer.R
import com.example.novaplayer.features.home.domain.model.HomeTabs
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.model.fakeTracks
import com.example.novaplayer.features.home.presentation.contract.HomeContract
import com.example.novaplayer.features.home.presentation.contract.LoadingState
import com.example.novaplayer.features.home.presentation.permission.AudioPermissionHandler
import com.example.novaplayer.features.home.presentation.screen.component.HomeTabBar
import com.example.novaplayer.features.miniplayer.presentation.MiniPlayerComp
import com.example.novaplayer.features.home.presentation.screen.component.Toolbar
import com.example.novaplayer.features.home.presentation.tabs.FavoritesTab
import com.example.novaplayer.features.home.presentation.tabs.PlayListTab
import com.example.novaplayer.features.home.presentation.tabs.RecentTab
import com.example.novaplayer.features.home.presentation.tabs.TracksTab
import com.example.novaplayer.features.home.presentation.viewmodel.HomeViewModel
import com.example.novaplayer.features.playlist.presentation.PlaylistIntent
import com.example.novaplayer.features.playlist.presentation.PlaylistState
import com.example.novaplayer.features.playlist.presentation.PlaylistViewModel
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy


@Composable
fun HomeSc(
    viewModel: HomeViewModel = hiltViewModel(),
    playlistViewModel: PlaylistViewModel = hiltViewModel(),
    onTrackClick: (Track) -> Unit,
    onPlaylistClick: (Long) -> Unit
) {
    val backdrop = rememberLayerBackdrop()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playlistState by playlistViewModel.state.collectAsStateWithLifecycle()


    AudioPermissionHandler(
        onPermissionGranted = {
            viewModel.onAction(
                HomeContract.UiAction.GetTracks
            )
        }
    )

    HomeContent(
        backdrop = backdrop,
        uiState = uiState,
        onTrackClick = onTrackClick,
        onPlaylistClick = onPlaylistClick,
        onAddPlaylistClick = {
            playlistViewModel.onIntent(
                PlaylistIntent.AddPlaylistClicked
            )
        },
        onPlaylistIntent = { intent ->
            playlistViewModel.onIntent(intent)
        },
        playlistState = playlistState
    )
    
}

@Composable
fun HomeContent(
    backdrop: LayerBackdrop,
    uiState: HomeContract.UiState,
    playlistState: PlaylistState,
    onTrackClick: (Track) -> Unit,
    onPlaylistClick: (Long) -> Unit,
    onAddPlaylistClick: () -> Unit,
    onPlaylistIntent: (PlaylistIntent) -> Unit

) {
    val backgroundColor = MaterialTheme.colorScheme.background
    var selectedTab by rememberSaveable {
        mutableStateOf(HomeTabs.TRACKS)
    }
    var showAddPlaylistIcon = selectedTab == HomeTabs.PLAYLISTS

    Scaffold(
        contentWindowInsets = WindowInsets.captionBar,
        containerColor = Color.Transparent,
        topBar = {
            Toolbar(
                showAddIcon = showAddPlaylistIcon,
                onAddPlaylistClick = onAddPlaylistClick
            )
        },

        ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .layerBackdrop(backdrop = backdrop),
            ) {

                HomeTabBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                when (selectedTab) {

                    HomeTabs.PLAYLISTS -> {

                        PlayListTab(
                            state = playlistState,
                            onIntent = onPlaylistIntent,
                            onPlaylistClick = onPlaylistClick
                        )
                    }

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

                                        Spacer(
                                            modifier = Modifier.height(8.dp)
                                        )

                                        LinearProgressIndicator(
                                            modifier = Modifier.width(120.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            LoadingState.SUCCESS -> {
                                TracksTab(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            16.dp
                                        ),
                                    tracks = uiState.tracks,
                                    onTrackClicked = onTrackClick
                                )
                            }

                            LoadingState.ERROR -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(
                                            R.string.error_loading_tracks
                                        ),
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }

                    HomeTabs.FAVORITES -> {

                        FavoritesTab()
                    }

                    HomeTabs.RECENT -> {

                        RecentTab()
                    }
                }
            }
            Box(
                modifier = Modifier

                    .fillMaxWidth()


                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )

                    .height(90.dp)
                    .align(Alignment.BottomCenter)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = {
                            CircleShape
                        },
                        effects = {
                            vibrancy()
                            blur(5f)
                            lens(16f.dp.toPx(), 32f.dp.toPx())
                        },
                        onDrawSurface = { drawRect(backgroundColor.copy(alpha = 0.8f)) }
                    )

            ) {
                MiniPlayerComp()
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    HomeContent(
        backdrop = rememberLayerBackdrop(),
        uiState = HomeContract.UiState(
            tracks = fakeTracks,
            loadingState = LoadingState.SUCCESS
        ),
        onTrackClick = {},
        onPlaylistClick = {},
        onAddPlaylistClick = {},
        playlistState = TODO(),
        onPlaylistIntent = TODO()
    )
}