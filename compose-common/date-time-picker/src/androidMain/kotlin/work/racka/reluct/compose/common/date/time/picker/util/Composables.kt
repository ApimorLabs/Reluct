@file:JvmName("ComposableJvm")

package work.racka.reluct.compose.common.date.time.picker.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
internal actual fun isSmallDevice(): Boolean = LocalConfiguration.current.screenWidthDp <= 360

@Composable
internal actual fun isLargeDevice(): Boolean = LocalConfiguration.current.screenWidthDp <= 600
