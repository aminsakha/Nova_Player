package com.example.novaplayer.core.locale

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.example.novaplayer.features.settings.domain.model.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
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
                context.getSystemService(android.app.LocaleManager::class.java)

            localeManager.applicationLocales =
                android.os.LocaleList.forLanguageTags(languageTag)
        } else {
            applyLegacyLocale(languageTag)
        }
    }

    private fun applyLegacyLocale(languageTag: String) {
        val locale = Locale.forLanguageTag(languageTag)

        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        context.resources.updateConfiguration(
            configuration,
            context.resources.displayMetrics
        )
    }
}