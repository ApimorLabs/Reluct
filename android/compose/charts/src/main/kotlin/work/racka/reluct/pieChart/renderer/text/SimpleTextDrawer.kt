package work.racka.reluct.pieChart.renderer.text

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import work.racka.reluct.common.toLegacyInt

class SimpleTextDrawer(
    private val labelTextSize: TextUnit = 18.sp,
    private val labelTextColor: Color = Color.Black
): TextDrawer {

    private val bounds = android.graphics.Rect()

    private val paint = android.graphics.Paint().apply {
        this.textAlign = android.graphics.Paint.Align.CENTER
        this.color = labelTextColor.toLegacyInt()
    }

    override fun drawText(
        drawScope: DrawScope,
        canvas: Canvas,
        text: String,
        area: Rect
    ) = with(drawScope) {
        val xCenter = area.left + (area.width / 2)
        val yCenter = (area.top + area.bottom) / 2

        canvas.nativeCanvas.drawText(
            text,
            xCenter,
            yCenter,
            paint.apply {
                this.textSize = labelTextSize.toPx()
            }
        )
    }


}