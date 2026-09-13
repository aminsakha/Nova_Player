package com.example.novaplayer.features.splash.presentation

interface SplashContract {

    data class State(
        val isLoading: Boolean = true
    )

    sealed interface Effect {
        data object NavigateToHome : Effect
        data object NavigateToPermission : Effect
    }
}