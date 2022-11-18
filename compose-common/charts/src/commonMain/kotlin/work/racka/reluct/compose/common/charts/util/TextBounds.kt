package work.racka.reluct.compose.common.charts.util

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint

/**
 * Computes the correct compose textBounds for desktop and Android with the
 * respective APIs
 */
internal expect fun Paint.getTextBounds(
    text: String,
    start: Int,
    end: Int,
    bounds: Rect,
    textSize: Float
): Rect