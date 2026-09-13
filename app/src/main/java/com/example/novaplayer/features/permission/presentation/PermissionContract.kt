package com.example.novaplayer.features.permission.presentation

interface PermissionContract {

    data class State(
        val isPrivacyAccepted: Boolean = false,
        val showPrivacyDialog: Boolean = false,
        val errorMessage: String? = null
    )

    sealed interface Event {
        data object OnPrivacyPolicyClicked : Event
        data object OnPrivacyDialogDismissed : Event
        data class OnPrivacyAcceptedChanged(val accepted: Boolean) : Event
        data object OnAllowPermissionClicked : Event
        data object OnPermissionGranted : Event
        data object OnPermissionDenied : Event
        data object OnNotNowClicked : Event
    }

    sealed interface Effect {
        data object RequestSystemPermission : Effect
        data object NavigateToHome : Effect
        data class ShowMessage(val message: String) : Effect
        data object ExitApp : Effect
    }
}