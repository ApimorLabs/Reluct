package work.racka.reluct.compose.common.charts.util

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toComposeRect

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
    /*if (start or end or end - start or text.length - end < 0) {
        throw IndexOutOfBoundsException()
    }*/

    val paint = this

    val nativeBounds = android.graphics.Rect(
        bounds.left.toInt(),
        bounds.top.toInt(),
        bounds.right.toInt(),
        bounds.bottom.toInt()
    )

    val nativePaint = paint.asFrameworkPaint().apply {
        this.textAlign = android.graphics.Paint.Align.CENTER
        this.textSize = textSize
    }

    nativePaint.getTextBounds(text, start, end, nativeBounds)
    return nativeBounds.toComposeRect()
}
