@file:JvmName("ColorDesktopJvm")

package work.racka.reluct.compose.common.components.util

import androidx.compose.ui.graphics.Color

actual fun Color.toLegacyInt(): Int {
    return org.jetbrains.skia.Color.makeARGB(
        (alpha * 255.0f + 0.5f).toInt(),
        (red * 255.0f + 0.5f).toInt(),
        (green * 255.0f + 0.5f).toInt(),
        (blue * 255.0f + 0.5f).toInt()
    )
}
