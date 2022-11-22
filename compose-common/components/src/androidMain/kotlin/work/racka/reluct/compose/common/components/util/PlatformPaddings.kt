package work.racka.reluct.compose.common.components.util

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier

/**
 * All Android platform specific paddings can be found here for ease of use in Common code
 * These paddings don't do anything on Desktop & Web side.
 */
actual fun Modifier.navigationBarsPadding() = navigationBarsPadding()
actual fun Modifier.statusBarsPadding(): Modifier = statusBarsPadding()
actual fun Modifier.imePadding(): Modifier = imePadding()
