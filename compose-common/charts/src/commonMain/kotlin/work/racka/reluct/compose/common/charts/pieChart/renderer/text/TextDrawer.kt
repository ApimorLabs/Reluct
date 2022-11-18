package work.racka.reluct.pieChart.renderer.text

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope

interface TextDrawer {

    fun drawText(
        drawScope: DrawScope,
        canvas: Canvas,
        text: String,
        area: Rect
    )
}