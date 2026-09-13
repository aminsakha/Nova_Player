package com.example.novaplayer.core.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.novaplayer.R
import java.util.Locale

val RokhFontFamily = FontFamily(
    Font(R.font.rokh_light, FontWeight.Light),
    Font(R.font.rokh_medium, FontWeight.Medium),
    Font(R.font.rokh_bold, FontWeight.Bold)
)

val VoyageFontFamily = FontFamily(
    Font(R.font.voyage_regular, FontWeight.Normal),
    Font(R.font.voyage_bold, FontWeight.Bold)
)

fun localizedFontFamily(): FontFamily {
    return if (Locale.getDefault().language == "fa") {
        RokhFontFamily
    } else {
        VoyageFontFamily
    }
}