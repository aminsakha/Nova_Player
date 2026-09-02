package com.example.novaplayer.features.settings.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.novaplayer.features.settings.domain.model.AppLanguage

@Composable
fun SettingsPanel(
    visible: Boolean,
    onClose: () -> Unit,
    onAboutClick: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { it }
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it }
        )
    ) {


        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
                    .clickable {
                        onClose()
                    }
            )

            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.68f)
                        .background(
                            MaterialTheme.colorScheme.background
                        )
                ) {
                    SettingsScreen(
                        onClose = onClose,
                        onAboutClick = onAboutClick,
                        onLanguageSelected = onLanguageSelected
                    )
                }
            }
        }
    }
}