package work.racka.reluct.compose.common.date.time.picker.core

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint

internal expect fun Canvas.drawTextHelper(
    text: String,
    x: Float,
    y: Float,
    paint: Paint,
    textSize: Float
)
