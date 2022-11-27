package work.racka.reluct.compose.common.date.time.picker.core

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Font
import org.jetbrains.skia.TextLine

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
    val textLine = TextLine.make(text, skiaFont)

    /**
     * If you want to handle other Alignments:
     * TextAlign.Left, TextAlign.Start -> 0f
     * TextAlign.Right, TextAlign.End -> textLine.width
     * TextAlign.Center, TextAlign.Justify -> textLine.width / 2f
     */
    val xOffset = textLine.width / 2f // Center Align text
    val actualX = x - xOffset
    this.nativeCanvas.drawTextLine(textLine, actualX, y, paint.asFrameworkPaint())
}
