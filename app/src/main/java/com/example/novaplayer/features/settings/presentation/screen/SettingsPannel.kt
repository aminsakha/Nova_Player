package com.example.novaplayer.features.settings.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.novaplayer.features.settings.domain.model.AppLanguage
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy



@Composable
fun SettingsPanel(
    backdrop: LayerBackdrop,
    visible: Boolean,
    onClose: () -> Unit,
    onAboutClick: () -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val color = MaterialTheme.colorScheme.background.copy(alpha = 0.92f)

    AnimatedVisibility(
        visible = visible,

        enter = slideInHorizontally(
            initialOffsetX = { fullWidth ->
                if (isRtl) {
                    -fullWidth
                } else {
                    fullWidth
                }
            }
        ),

        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth ->
                if (isRtl) {
                    -fullWidth
                } else {
                    fullWidth
                }
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // Backdrop
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        onClose()
                    }
            )

            // Settings Panel
            Box(
                modifier = Modifier
                    // پنل همیشه سمت راست است
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .fillMaxWidth(0.68f)
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = {
                            RoundedCornerShape(16.dp)
                        },
                        effects = {
                            vibrancy()
                            blur(8f.dp.toPx())
                            lens(
                                16f.dp.toPx(),
                                32f.dp.toPx()
                            )
                        },
                        onDrawSurface = {
                            drawRect(color)
                        }
                    )
                    .background(Color.Transparent)
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


