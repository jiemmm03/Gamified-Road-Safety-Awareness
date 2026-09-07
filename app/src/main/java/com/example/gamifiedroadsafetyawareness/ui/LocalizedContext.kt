package com.example.gamifiedroadsafetyawareness.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * Wraps [content] in a Context whose resources are overridden to [languageCode] — every
 * `stringResource()` call inside resolves against that locale and recomposes instantly when
 * [languageCode] changes, with no Activity recreation (which would blow away RoadSafetyApp's
 * in-memory navigation stack). This is the app's first use of CompositionLocalProvider —
 * introduced here because it's the correct tool for in-app language switching, not a workaround.
 */
@Composable
fun ProvideLocalizedContext(languageCode: String, content: @Composable () -> Unit) {
    val baseContext = LocalContext.current
    val localizedContext = remember(baseContext, languageCode) {
        val configuration = Configuration(baseContext.resources.configuration).apply {
            setLocale(Locale(languageCode))
        }
        baseContext.createConfigurationContext(configuration)
    }
    CompositionLocalProvider(LocalContext provides localizedContext) {
        content()
    }
}
