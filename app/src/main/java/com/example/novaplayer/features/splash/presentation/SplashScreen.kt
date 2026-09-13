package com.example.novaplayer.features.splash.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.novaplayer.R
import com.example.novaplayer.core.ui.theme.localizedFontFamily

@Composable
fun SplashScreen(
    viewModel: SplashViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToPermission: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SplashContract.Effect.NavigateToHome -> onNavigateToHome()
                SplashContract.Effect.NavigateToPermission -> onNavigateToPermission()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.background,
                        colors.surface,
                        colors.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.nova_transparent),
                contentDescription = "Nova Player Logo",
                modifier = Modifier.size(260.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Nova Player",
                color = colors.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = localizedFontFamily(),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "A Timeless Listening Experience.",
                color = colors.onSurface.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(id = R.drawable.audiowave),
                contentDescription = "Audio wave icon",
                modifier = Modifier.size(60.dp)
            )
        }
    }
}