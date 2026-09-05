package com.example.novaplayer.features.home.domain.model

import androidx.annotation.StringRes
import com.example.novaplayer.R

enum class HomeTabs(
    @StringRes val titleRes: Int
) {
    PLAYLISTS(R.string.home_tab_playlists),
    TRACKS(R.string.home_tab_tracks),
    FAVORITES(R.string.home_tab_favorites),
    RECENT(R.string.home_tab_recent)
}