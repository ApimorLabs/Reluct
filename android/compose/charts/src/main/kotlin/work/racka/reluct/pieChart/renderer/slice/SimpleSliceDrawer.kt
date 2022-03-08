package work.racka.reluct.pieChart.renderer.slice

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import work.racka.reluct.pieChart.PieChartData

class SimpleSliceDrawer(
    private val sliceThickness: Float = 25f
) : SliceDrawer {

    init {
        require(sliceThickness in 10f..100f) {
            "Thickness of $sliceThickness must be between 10-100"
        }
    }

    private val sectionPaint = Paint().apply {
        isAntiAlias = true
        style = PaintingStyle.Stroke
    }

    override fun drawSlice(
        drawScope: DrawScope,
        canvas: Canvas,
        area: Size,
        startAngle: Float,
        sweepAngle: Float,
        slice: PieChartData.Slice
    ) {
        val sliceThickness = calculateSectorThickness(area = area)
        val drawableArea = calculateDrawableArea(area = area)

        val clipRect = Rect(
            left = drawableArea.left - drawableArea.left * .2f,
            top = drawableArea.top - drawableArea.top * .2f,
            right = drawableArea.right - drawableArea.right * .2f,
            bottom = drawableArea.bottom - drawableArea.bottom * .2f
        )

        val path = Path()
//        path.addRoundRect(
//            RoundRect(
//                drawableArea,
//                topLeft = CornerRadius(cornerRadius),
//                topRight = CornerRadius(cornerRadius),
//                bottomLeft = CornerRadius.Zero,
//                bottomRight = CornerRadius.Zero
//            )
//        )
        path.addArc(
            oval = drawableArea,
            startAngleDegrees = startAngle,
            sweepAngleDegrees = sweepAngle
        )


//        canvas.drawPath(
//            path = path,
//            paint = sectionPaint.apply {
//                color = Color.Transparent
//                strokeWidth = sliceThickness
//            }
//        )

        canvas.drawArc(
            rect = drawableArea,
            paint = sectionPaint.apply {
                color = slice.color
                strokeWidth = sliceThickness
            },
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false
        )
    }

    private fun calculateSectorThickness(area: Size): Float {
        val minSize = minOf(area.width, area.height)

        return minSize * (sliceThickness / 200f)
    }

    private fun calculateDrawableArea(area: Size): Rect {
        val sliceThicknessOffset =
            calculateSectorThickness(area = area) / 2f
        val offsetHorizontally = (area.width - area.height) / 2f

        return Rect(
            left = sliceThicknessOffset + offsetHorizontally,
            top = sliceThicknessOffset,
            right = area.width - sliceThicknessOffset - offsetHorizontally,
            bottom = area.height - sliceThicknessOffset
        )
    }
}