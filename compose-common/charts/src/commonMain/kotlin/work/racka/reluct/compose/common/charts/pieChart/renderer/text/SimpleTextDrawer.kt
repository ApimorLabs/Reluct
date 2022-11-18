package work.racka.reluct.compose.common.charts.pieChart.renderer.text

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import work.racka.reluct.compose.common.charts.util.drawTextHelper

class SimpleTextDrawer(
    private val labelTextSize: TextUnit = 18.sp,
    private val labelTextColor: Color = Color.Black
) : TextDrawer {

    // private val bounds = Rect.Zero

    private val paint = Paint().apply {
        this.color = labelTextColor
    }

    override fun drawText(
        drawScope: DrawScope,
        canvas: Canvas,
        text: String,
        area: Rect
    ) = with(drawScope) {
        val xCenter = area.left + (area.width / 2)
        val yCenter = (area.top + area.bottom) / 2

        canvas.drawTextHelper(
            text,
            xCenter,
            yCenter,
            paint,
            labelTextSize.toPx()
        )
    }
}
