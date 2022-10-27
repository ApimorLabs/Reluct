package work.racka.reluct.android.compose.components.util

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColorInt
import kotlin.random.Random

fun Color.toHexString(): String {
    val red = this.red * 255
    val green = this.green * 255
    val blue = this.blue * 255
    return String.format("#%02x%02x%02x", red.toInt(), green.toInt(), blue.toInt())
}

fun String.toColor(): Color {
    val containsHash = this[0] == '#'
    val colorString = if (containsHash) this else "#$this"
    return try {
        Color(colorString.toColorInt())
    } catch (e: Exception) {
        e.printStackTrace()
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
    val colorInt = this.toColorInt()
    val whiteContrast = ColorUtils.calculateContrast(android.graphics.Color.WHITE, colorInt)
    val blackContrast = ColorUtils.calculateContrast(android.graphics.Color.BLACK, colorInt)
    return if (whiteContrast > blackContrast) Color.White else Color.Black
}

internal fun Color.toColorInt(): Int {
    return android.graphics.Color.argb(
        (alpha * 255.0f + 0.5f).toInt(),
        (red * 255.0f + 0.5f).toInt(),
        (green * 255.0f + 0.5f).toInt(),
        (blue * 255.0f + 0.5f).toInt()
    )
}