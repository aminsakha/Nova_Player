package com.example.novaplayer.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import com.example.novaplayer.core.locale.AppLocaleManager
import com.example.novaplayer.features.settings.domain.usecase.ObserveLanguageUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var observeLanguageUseCase: ObserveLanguageUseCase

    @Inject
    lateinit var appLocaleManager: AppLocaleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        lifecycleScope.launch {
            val savedLanguage = observeLanguageUseCase().first()

            appLocaleManager.setLanguage(savedLanguage)

            setContent {
                MusicPlayerApp()
            }
        }
    }
}