package com.example.novaplayer.features.permission.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novaplayer.features.permission.domain.usecase.SetPrivacyAcceptedUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PermissionViewModel(
    private val setPrivacyAcceptedUseCase: SetPrivacyAcceptedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PermissionContract.State())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PermissionContract.Effect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: PermissionContract.Event) {
        when (event) {
            PermissionContract.Event.OnPrivacyPolicyClicked -> {
                _state.update { it.copy(showPrivacyDialog = true) }
            }

            PermissionContract.Event.OnPrivacyDialogDismissed -> {
                _state.update { it.copy(showPrivacyDialog = false) }
            }

            is PermissionContract.Event.OnPrivacyAcceptedChanged -> {
                _state.update {
                    it.copy(
                        isPrivacyAccepted = event.accepted,
                        errorMessage = null
                    )
                }
            }

            PermissionContract.Event.OnAllowPermissionClicked -> {
                handleAllowPermissionClick()
            }

            PermissionContract.Event.OnPermissionGranted -> {
                handlePermissionGranted()
            }

            PermissionContract.Event.OnPermissionDenied -> {
                viewModelScope.launch {
                    _effect.emit(
                        PermissionContract.Effect.ShowMessage(
                            "Permission denied. You can allow it later from settings."
                        )
                    )
                }
            }

            PermissionContract.Event.OnNotNowClicked -> {
                viewModelScope.launch {
                    _effect.emit(PermissionContract.Effect.ExitApp)
                }
            }
        }
    }

    private fun handleAllowPermissionClick() {
        viewModelScope.launch {
            val accepted = _state.value.isPrivacyAccepted

            if (!accepted) {
                _effect.emit(
                    PermissionContract.Effect.ShowMessage(
                        "Please accept the privacy policy first."
                    )
                )
                return@launch
            }

            setPrivacyAcceptedUseCase(true)
            _effect.emit(PermissionContract.Effect.RequestSystemPermission)
        }
    }

    private fun handlePermissionGranted() {
        viewModelScope.launch {
            _effect.emit(PermissionContract.Effect.NavigateToHome)
        }
    }
}