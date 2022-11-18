package work.racka.reluct.compose.common.charts.util

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toComposeRect
import org.jetbrains.skia.Font

/**
 * Computes the correct compose textBounds for desktop and Android with the
 * respective APIs
 */
internal actual fun Paint.getTextBounds(
    text: String,
    start: Int,
    end: Int,
    bounds: Rect,
    textSize: Float
): Rect {
    if (start or end or end - start or text.length - end < 0) {
        throw IndexOutOfBoundsException()
    }

    val paint = this
    val skiaFont = Font().apply {
        this.size = textSize
    }
    val glyph = skiaFont.getStringGlyphs(text)
    bounds.bottom
    val boundsArray = skiaFont.getBounds(glyph, paint.asFrameworkPaint())
    var textRect = Rect.Zero
    boundsArray.forEach { rect ->
        val composeRect = rect.toComposeRect()
        textRect = Rect(
            left = textRect.left + composeRect.left,
            top = textRect.top + composeRect.top,
            right = textRect.right + composeRect.right,
            bottom = textRect.bottom + composeRect.bottom
        )
    }
    return Rect(
        left = textRect.left + bounds.left,
        top = textRect.top + bounds.top,
        right = textRect.right + bounds.right,
        bottom = textRect.bottom + bounds.bottom
    )
}
