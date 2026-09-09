package com.example.gamifiedroadsafetyawareness.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * Wraps [content] in a Context and Configuration whose resources are overridden to [languageCode] — every
 * `stringResource()` call inside resolves against that locale and recomposes instantly when
 * [languageCode] changes, with no Activity recreation (which would blow away RoadSafetyApp's
 * in-memory navigation stack).
 */
@Composable
fun ProvideLocalizedContext(languageCode: String, content: @Composable () -> Unit) {
    val baseContext = LocalContext.current
    val localizedContext = remember(baseContext, languageCode) {
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        val configuration = Configuration(baseContext.resources.configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
        baseContext.createConfigurationContext(configuration)
    }
    val localizedConfiguration = remember(localizedContext) {
        localizedContext.resources.configuration
    }
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalConfiguration provides localizedConfiguration
    ) {
        content()
    }
}
