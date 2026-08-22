package com.example.novaplayer.features.home.presentation.viewmodel

import com.example.novaplayer.features.home.domain.usecase.GetTracksUseCase
import com.example.novaplayer.features.settings.domain.usecase.ObserveThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val trackUseCase: GetTracksUseCase
) {

}