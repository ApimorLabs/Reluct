package work.racka.reluct.compose.common.components.util

import androidx.compose.ui.Modifier

/**
 * All Android platform specific paddings can be found here for ease of use in Common code
 * These paddings don't do anything on Desktop & Web side.
 *
 * For more information on these please visit the Android dev site regarding Insets
 */

expect fun Modifier.navigationBarsPadding(): Modifier
expect fun Modifier.statusBarsPadding(): Modifier
expect fun Modifier.imePadding(): Modifier
