package com.example.novaplayer.features.home.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.features.home.domain.model.Track
import com.example.novaplayer.features.home.domain.usecase.GetTracksUseCase
import com.example.novaplayer.features.home.presentation.contract.LoadingState
import com.example.novaplayer.features.home.presentation.contract.HomeContract
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
    private val _track = MutableStateFlow<Track?>(null)
    val track = _track.asStateFlow()
    private val _uiState = MutableStateFlow(
        HomeContract.UiState()
    )

    val uiState = _uiState.asStateFlow()

    fun onAction(action: HomeContract.UiAction) {
        when (action) {
            is HomeContract.UiAction.GetTracks -> getTracks()
            is HomeContract.UiAction.GetTrack -> loadTrack(action.uri)
        }
    }
    fun loadTrack(uri: String) {
        viewModelScope.launch {
            _track.value = getTracksUseCase.getTrack(uri)
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
                val tracks =getTracksUseCase.getAllTrack()

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
                Log.d("ERROR",e.message.toString())

            }
        }
    }
}