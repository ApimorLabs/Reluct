package work.racka.reluct.compose.common.components.util

import androidx.compose.ui.Modifier

/**
 * All Android platform specific paddings can be found here for ease of use in Common code
 * These paddings don't do anything on Desktop & Web side.
 */

actual fun Modifier.navigationBarsPadding(): Modifier = this
actual fun Modifier.statusBarsPadding(): Modifier = this
actual fun Modifier.imePadding(): Modifier = this