package work.racka.reluct.barChart.renderer.xaxis


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.roundToInt

class SimpleXAxisDrawer(
    private val labelTextSize: TextUnit = 12.sp,
    private val labelRatio: Int = 3,
    private val axisLineThickness: Dp = 1.dp,
    private val axisLineColor: Color = Color.Black
) : XAxisDrawer {
    private val bounds = android.graphics.Rect()

    private val axisLinePaint = Paint().apply {
        isAntiAlias = true
        color = axisLineColor
        style = PaintingStyle.Stroke
    }

    override fun requiredHeight(drawScope: DrawScope): Float = with(drawScope) {
        (3f / 2f) * axisLineThickness.toPx()
    }

    override fun drawAxisLine(drawScope: DrawScope, canvas: Canvas, drawableArea: Rect) =
        with(drawScope) {
            val lineThickness = axisLineThickness.toPx()
            val y = drawableArea.top + (lineThickness / 2f)

            canvas.drawLine(
                p1 = Offset(
                    x = drawableArea.left,
                    y = y
                ),
                p2 = Offset(
                    x = drawableArea.right,
                    y = y
                ),
                paint = axisLinePaint.apply {
                    strokeWidth = lineThickness
                }
            )
        }

    override fun drawIntervalLines(drawScope: DrawScope, canvas: Canvas, drawableArea: Rect) =
        with(drawScope) {
            val minLabelHeight = (labelTextSize.toPx() * labelRatio.toFloat())
            val totalHeight = drawableArea.height
            val labelCount = max((drawableArea.height / minLabelHeight).roundToInt(), 2)
            val lineThickness = axisLineThickness.toPx()

            for (i in 1..labelCount) {

                val y =
                    drawableArea.bottom - (i * (totalHeight / labelCount)) + (bounds.height() / 2f)

                canvas.drawLine(
                    p1 = Offset(
                        x = drawableArea.left,
                        y = y
                    ),
                    p2 = Offset(
                        x = drawableArea.right,
                        y = y
                    ),
                    paint = axisLinePaint.apply {
                        strokeWidth = lineThickness
                    }
                )
            }

        }
}