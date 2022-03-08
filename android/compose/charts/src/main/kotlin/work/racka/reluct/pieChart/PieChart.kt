package work.racka.reluct.pieChart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import timber.log.Timber
import work.racka.reluct.common.simpleChartAnimation
import work.racka.reluct.pieChart.PieChartUtils.calculateAngle
import work.racka.reluct.pieChart.renderer.slice.SimpleSliceDrawer
import work.racka.reluct.pieChart.renderer.slice.SliceDrawer
import work.racka.reluct.pieChart.renderer.text.SimpleTextDrawer
import work.racka.reluct.pieChart.renderer.text.TextDrawer

@Composable
fun PieChart(
    pieChartData: PieChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    sliceDrawer: SliceDrawer = SimpleSliceDrawer(),
    centerTextDrawer: TextDrawer = SimpleTextDrawer(),
    centerText: String = "",
    onCenterClick: () -> Unit = { }
) {
    val transitionProgress =
        remember(pieChartData.slicesWithSpacing) { Animatable(initialValue = 0f) }

    // When slices value changes we want to re-animate the chart.
    LaunchedEffect(pieChartData.slicesWithSpacing) {
        transitionProgress.animateTo(1f, animationSpec = animation)
    }

    DrawChart(
        pieChartData = pieChartData,
        modifier = modifier.fillMaxSize(),
        progress = transitionProgress.value,
        sliceDrawer = sliceDrawer,
        centerTextDrawer = centerTextDrawer,
        centerText = centerText,
        onCenterClick = onCenterClick
    )
}

@Composable
private fun DrawChart(
    pieChartData: PieChartData,
    modifier: Modifier,
    progress: Float,
    sliceDrawer: SliceDrawer,
    centerTextDrawer: TextDrawer,
    centerText: String,
    onCenterClick: () -> Unit
) {
    val slices = pieChartData.slicesWithSpacing
    val centerRect = remember {
        mutableStateOf(Rect.Zero)
    }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        if (centerRect.value.contains(offset)) {
                            Timber.d("Center Rect Clicked")
                            onCenterClick()
                        }
                    }
                )
            }
    ) {
        drawIntoCanvas {
            var startArc = -90f
            centerRect.value = size.toRect()

            slices.forEach { slice ->
                val arc = calculateAngle(
                    sliceLength = slice.value,
                    totalLength = pieChartData.totalSize,
                    progress = progress
                )

                sliceDrawer.drawSlice(
                    drawScope = this,
                    canvas = drawContext.canvas,
                    area = size,
                    startAngle = startArc,
                    sweepAngle = arc,
                    slice = slice
                )

                startArc += arc
            }

            centerTextDrawer.drawText(
                drawScope = this,
                canvas = drawContext.canvas,
                text = centerText,
                area = size.toRect()
            )
        }
    }
}

@Preview
@Composable
fun PieChartPreview() = PieChart(
    pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice(25f, Color.Red),
            PieChartData.Slice(42f, Color.Blue),
            PieChartData.Slice(23f, Color.Green)
        )
    )
)