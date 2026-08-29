package com.example.novaplayer.features.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.novaplayer.R
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.miniplayer.presentation.MiniPlayerComp
import com.example.novaplayer.features.search.components.RecentSearchItem
import com.example.novaplayer.features.search.components.SearchResultItem
import com.example.novaplayer.features.search.components.SearchTopBar
import com.example.novaplayer.features.search.presentation.contract.SearchContract
import com.example.novaplayer.features.search.presentation.contract.SearchError

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onTrackClick: (Track) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            SearchTopBar(
                query = uiState.query,
                onQueryChanged = { query ->
                    viewModel.onAction(
                        SearchContract.UiAction.QueryChanged(
                            query
                        )
                    )
                },
                onBackClick = onBackClick,
                onSubmitSearch = {
                    viewModel.onAction(
                        SearchContract.UiAction.SubmitSearch
                    )
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(98.dp)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    )
            ) {
                MiniPlayerComp(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 8.dp,
                top = innerPadding.calculateTopPadding() +
                        8.dp,
                end = 8.dp,
                bottom = innerPadding.calculateBottomPadding() +
                        8.dp
            ),
            verticalArrangement =
                Arrangement.spacedBy(2.dp)
        ) {
            if (uiState.recentSearches.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(
                            R.string.recent_searches
                        ),
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                        style =
                            MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                items(
                    items = uiState.recentSearches,
                    key = { query -> query }
                ) { query ->
                    RecentSearchItem(
                        query = query,
                        onClick = {
                            viewModel.onAction(
                                SearchContract.UiAction
                                    .RecentSearchSelected(
                                        query
                                    )
                            )
                        },
                        onDeleteClick = {
                            viewModel.onAction(
                                SearchContract.UiAction
                                    .DeleteRecentSearch(
                                        query
                                    )
                            )
                        }
                    )
                }
            }

            item {
                Text(
                    text = stringResource(
                        R.string.search_results
                    ),
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 8.dp
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }

            when {
                uiState.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                uiState.error ==
                        SearchError.PERMISSION_DENIED -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment =
                                Alignment.CenterHorizontally,
                            verticalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(
                                    R.string
                                        .search_permission_required
                                ),
                                color =
                                    MaterialTheme.colorScheme.error
                            )

                            Button(
                                onClick = {
                                    viewModel.onAction(
                                        SearchContract.UiAction.Retry
                                    )
                                }
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.retry
                                    )
                                )
                            }
                        }
                    }
                }

                uiState.hasNoResults -> {
                    item {
                        Text(
                            text = stringResource(
                                R.string.no_search_results
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    items(
                        items = uiState.searchResults,
                        key = { track -> track.uri }
                    ) { track ->
                        SearchResultItem(
                            track = track,
                            onClick = {
                                viewModel.onAction(
                                    SearchContract.UiAction
                                        .TrackSelected(track)
                                )

                                onTrackClick(track)
                            }
                        )
                    }
                }
            }
        }
    }
}
