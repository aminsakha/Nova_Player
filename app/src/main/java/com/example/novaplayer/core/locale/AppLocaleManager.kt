package com.example.novaplayer.core.locale

import android.app.Activity
import android.app.LocaleManager
import android.content.res.Configuration
import android.os.Build
import com.example.novaplayer.features.settings.domain.model.AppLanguage
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLocaleManager @Inject constructor() {

    fun setLanguage(
        activity: Activity,
        language: AppLanguage
    ) {
        val languageTag = when (language) {
            AppLanguage.EN -> "en"
            AppLanguage.FA -> "fa"
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager =
                activity.getSystemService(LocaleManager::class.java)

            localeManager.applicationLocales =
                android.os.LocaleList.forLanguageTags(languageTag)
        } else {
            applyLegacyLocale(activity, languageTag)
        }
    }

    private fun applyLegacyLocale(
        activity: Activity,
        languageTag: String
    ) {
        val currentLanguage =
            activity.resources.configuration.locale.language
        
        if (currentLanguage == languageTag) {
            return
        }

        val locale = Locale.forLanguageTag(languageTag)

        Locale.setDefault(locale)

        val configuration =
            Configuration(activity.resources.configuration)

        configuration.setLocale(locale)

        activity.resources.updateConfiguration(
            configuration,
            activity.resources.displayMetrics
        )

        activity.recreate()
    }
}