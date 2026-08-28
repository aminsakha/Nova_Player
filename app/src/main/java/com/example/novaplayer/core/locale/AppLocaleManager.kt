package com.example.novaplayer.core.locale

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.novaplayer.features.settings.domain.model.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocaleManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun setLanguage(language: AppLanguage) {
        val languageTag = when (language) {
            AppLanguage.EN -> "en"
            AppLanguage.FA -> "fa"
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager =
                context.getSystemService(LocaleManager::class.java)

            localeManager.applicationLocales =
                android.os.LocaleList.forLanguageTags(languageTag)

        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(languageTag)
            )
        }
    }
}