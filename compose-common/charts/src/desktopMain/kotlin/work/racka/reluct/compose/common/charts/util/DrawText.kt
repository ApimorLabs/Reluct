package work.racka.reluct.compose.common.charts.util

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Font

internal actual fun Canvas.drawTextHelper(
    text: String,
    x: Float,
    y: Float,
    paint: Paint,
    textSize: Float
) {
    val skiaFont = Font().apply {
        this.size = textSize
    }
    this.nativeCanvas.drawString(text, x, y, skiaFont, paint.asFrameworkPaint())
}
