package com.example.novaplayer.features.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.features.home.domain.usecase.GetTracksUseCase
import com.example.novaplayer.features.home.presentation.contract.LoadingState
import com.example.novaplayer.features.home.presentation.contract.TrackContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTracksUseCase: GetTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TrackContract.UiState()
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: TrackContract.UiAction) {
        when (action) {
            TrackContract.UiAction.GetTracks -> getTracks()
        }
    }

    private fun getTracks() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    loadingState = LoadingState.LOADING
                )
            }

            try {
                val tracks = getTracksUseCase()

                _uiState.update {
                    it.copy(
                        tracks = tracks,
                        loadingState = LoadingState.SUCCESS
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingState = LoadingState.ERROR
                    )
                }
            }
        }
    }
}