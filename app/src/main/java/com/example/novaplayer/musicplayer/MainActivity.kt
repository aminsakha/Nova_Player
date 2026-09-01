package com.example.novaplayer.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.novaplayer.core.locale.AppLocaleManager
import com.example.novaplayer.features.settings.domain.usecase.ObserveLanguageUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        setContent {
            MusicPlayerApp()
        }
        observeLanguage()
    }

    private fun observeLanguage() {
        lifecycleScope.launch {
            observeLanguageUseCase().collect { language ->
                appLocaleManager.setLanguage(
                    activity = this@MainActivity,
                    language = language
                )
            }
        }
    }
}