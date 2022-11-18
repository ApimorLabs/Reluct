package work.racka.reluct.compose.common.charts.util

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import work.racka.reluct.compose.common.charts.common.toLegacyInt

internal actual fun Canvas.drawTextHelper(
    text: String,
    x: Float,
    y: Float,
    paint: Paint,
    textSize: Float
) {
    val newPaint = android.graphics.Paint().apply {
        this.textSize = textSize
        this.textAlign = android.graphics.Paint.Align.CENTER
        this.color = paint.color.toLegacyInt()
    }
    this.nativeCanvas.drawText(text, x, y, newPaint)
}
