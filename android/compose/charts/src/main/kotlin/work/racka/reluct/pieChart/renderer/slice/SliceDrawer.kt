package work.racka.reluct.pieChart.renderer.slice

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import work.racka.reluct.pieChart.PieChartData

interface SliceDrawer {
    fun drawSlice(
        drawScope: DrawScope,
        canvas: Canvas,
        area: Size,
        startAngle: Float,
        sweepAngle: Float,
        slice: PieChartData.Slice
    )
}