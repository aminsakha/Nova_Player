package com.example.novaplayer.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.features.permission.domain.usecase.CheckMusicPermissionUseCase
import com.example.novaplayer.features.permission.domain.usecase.GetPrivacyAcceptedUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val checkMusicPermissionUseCase: CheckMusicPermissionUseCase,
    private val getPrivacyAcceptedUseCase: GetPrivacyAcceptedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SplashContract.State())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SplashContract.Effect>()
    val effect = _effect.asSharedFlow()

    init {
        decideNavigation()
    }

    private fun decideNavigation() {
        viewModelScope.launch {
            delay(2000)

            val isPrivacyAccepted = getPrivacyAcceptedUseCase()
            val isPermissionGranted = checkMusicPermissionUseCase()

            if (isPrivacyAccepted && isPermissionGranted) {
                _effect.emit(SplashContract.Effect.NavigateToHome)
            } else {
                _effect.emit(SplashContract.Effect.NavigateToPermission)
            }
        }
    }
}