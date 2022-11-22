package work.racka.reluct.compose.common.components.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import co.touchlab.kermit.Logger
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

fun Color.toHexString(): String {
    val red = this.red * 255
    val green = this.green * 255
    val blue = this.blue * 255
    return String.format(Locale.US, "#%02x%02x%02x", red.toInt(), green.toInt(), blue.toInt())
}

fun String.toColor(): Color {
    val containsHash = this[0] == '#'
    val colorString = if (containsHash) this else "#$this"
    return try {
        Color(colorString.toColorInt())
    } catch (e: Exception) {
        Logger.d("Color error: ${e.message}")
        Color.White
    }
}

fun getRandomColor(): Color = Color(
    red = Random.nextInt(256),
    green = Random.nextInt(256),
    blue = Random.nextInt(256)
)

/**
 * See https://stackoverflow.com/a/60912606/15285215 for more details
 */
fun Color.getContentColor(): Color {
    val whiteContrast = calculateContrast(Color.White, this)
    val blackContrast = calculateContrast(Color.Black, this)
    return if (whiteContrast > blackContrast) Color.White else Color.Black
}

/**
 * Returns the contrast ratio between [foreground] and [background].
 * [background] must be opaque.
 * Note: If it's not opaque it will be made opaque
 * <p>
 * Formula defined
 * <a href="http://www.w3.org/TR/2008/REC-WCAG20-20081211/#contrast-ratiodef">here</a>.
 */
fun calculateContrast(foreground: Color, background: Color): Double {
    val newBg = if (background.alpha != 1f) background.copy(alpha = 1f) else background
    // If the foreground is translucent, composite the foreground over the background
    val newFg = if (foreground.alpha < 1f) foreground.compositeOver(newBg) else foreground
    val luminance1 = newFg.luminance() + 0.05
    val luminance2 = newBg.luminance() + 0.05
    return max(luminance1, luminance2) / min(luminance1, luminance2)
}

/**
 * Returns ARGB color value
 */
expect fun Color.toLegacyInt(): Int

fun String.toColorInt(): Int {
    if (this[0] == '#') { // Use a long to avoid rollovers on #ffXXXXXX
        var color = this.substring(1).toLong(16)
        if (this.length == 7) { // Set the alpha value
            color = color or -0x1000000
        } else {
            require(this.length == 9) { "Unknown color" }
        }
        return color.toInt()
    }
    throw IllegalArgumentException("Unknown color")
}
