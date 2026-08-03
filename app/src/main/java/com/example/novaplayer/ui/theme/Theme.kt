package com.example.novaplayer.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import com.example.novaplayer.ui.theme.*


private val DarkColorScheme = darkColorScheme(

    primary = NovaPrimary,
    onPrimary = Color.White,

    primaryContainer = NovaPrimaryVariant,
    onPrimaryContainer = Color.White,

    secondary = NovaSecondary,
    onSecondary = Color.Black,

    tertiary = NovaAccent,
    onTertiary = Color.Black,


    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,


    error = ErrorRed,
    onError = Color.White
)



private val LightColorScheme = lightColorScheme(

    primary = NovaPrimary,
    onPrimary = Color.White,

    primaryContainer = Color(0xFFE9D5FF),
    onPrimaryContainer = Color(0xFF4C1D95),

    secondary = NovaSecondary,
    onSecondary = Color.Black,

    tertiary = NovaAccent,
    onTertiary = Color.Black,


    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,


    error = ErrorRed,
    onError = Color.White
)



@Composable
fun NovaPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}