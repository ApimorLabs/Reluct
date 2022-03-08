package work.racka.reluct.barChart.renderer.bar

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import work.racka.reluct.barChart.BarChartData

class SimpleBarDrawer(
    private val cornerRadius: Float = 20f
) : BarDrawer {
    private val barPaint = Paint().apply {
        this.isAntiAlias = true
    }

    override fun drawBar(
        drawScope: DrawScope,
        canvas: Canvas,
        barArea: Rect,
        bar: BarChartData.Bar,
        selected: Boolean,
        selectedBarColor: Color
    ) {
        val path = Path()
        path.addRoundRect(
            RoundRect(
                barArea,
                topLeft = CornerRadius(cornerRadius),
                topRight = CornerRadius(cornerRadius),
                bottomLeft = CornerRadius.Zero,
                bottomRight = CornerRadius.Zero
            )
        )



        canvas.drawPath(
            path,
            barPaint.apply {
                color = if (selected) selectedBarColor else bar.color
            }
        )
    }
}