package work.racka.reluct.barChart

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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import work.racka.reluct.barChart.BarChartUtils.axisAreas
import work.racka.reluct.barChart.BarChartUtils.barDrawableArea
import work.racka.reluct.barChart.BarChartUtils.forEachWithArea
import work.racka.reluct.barChart.renderer.bar.BarDrawer
import work.racka.reluct.barChart.renderer.bar.SimpleBarDrawer
import work.racka.reluct.barChart.renderer.label.LabelDrawer
import work.racka.reluct.barChart.renderer.label.SimpleValueDrawer
import work.racka.reluct.barChart.renderer.xaxis.SimpleXAxisDrawer
import work.racka.reluct.barChart.renderer.xaxis.XAxisDrawer
import work.racka.reluct.barChart.renderer.yaxis.SimpleYAxisDrawer
import work.racka.reluct.barChart.renderer.yaxis.YAxisDrawer
import work.racka.reluct.common.simpleChartAnimation

@Composable
fun BarChart(
    barChartData: BarChartData,
    modifier: Modifier = Modifier,
    animation: AnimationSpec<Float> = simpleChartAnimation(),
    barChartOptions: BarChartOptions = BarChartOptions(),
    barDrawer: BarDrawer = SimpleBarDrawer(),
    xAxisDrawer: XAxisDrawer = SimpleXAxisDrawer(),
    yAxisDrawer: YAxisDrawer = SimpleYAxisDrawer(),
    labelDrawer: LabelDrawer = SimpleValueDrawer(),
    onBarClicked: (id: Int) -> Unit = { },
    selectedBarColor: Color = Color.Blue,
    selectedUniqueId: Int? = null,
) {
    val transitionAnimation = remember(barChartData.bars) { Animatable(initialValue = 0f) }
    val selectedBar = remember {
        mutableStateOf(-1)
    }
    val bars = remember {
        mutableMapOf<Int, Rect>()
    }

    LaunchedEffect(barChartData.bars) {
        transitionAnimation.animateTo(1f, animationSpec = animation)
    }

    val progress = transitionAnimation.value

    Canvas(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { offset ->
                    bars.forEach { (key, rect) ->
                        if (rect.contains(offset)) {
                            selectedBar.value = key
                            onBarClicked(key)
                        }
                    }
                }
            )
        }
        .drawBehind {
            drawIntoCanvas { canvas ->
                val (xAxisArea, yAxisArea) = axisAreas(
                    drawScope = this,
                    totalSize = size,
                    withYAxisLabels = barChartOptions.showYAxisLabels,
                    xAxisDrawer = xAxisDrawer,
                    labelDrawer = labelDrawer
                )
                val barDrawableArea = barDrawableArea(xAxisArea)

                // Draw yAxis line.
                if (barChartOptions.drawYAxisLine) {
                    yAxisDrawer.drawAxisLine(
                        drawScope = this,
                        canvas = canvas,
                        drawableArea = yAxisArea
                    )
                }

                // Draw Interval lines
                if (barChartOptions.showIntervalLines) {
                    xAxisDrawer.drawIntervalLines(
                        drawScope = this,
                        canvas = canvas,
                        drawableArea = barDrawableArea
                    )
                }

                // Draw xAxis line.
                if (barChartOptions.drawXAxisLine) {
                    xAxisDrawer.drawAxisLine(
                        drawScope = this,
                        canvas = canvas,
                        drawableArea = xAxisArea
                    )
                }

                // Draw each bar.
                barChartData.forEachWithArea(
                    this,
                    barDrawableArea,
                    progress,
                    labelDrawer,
                    barChartOptions.barsSpacingFactor
                ) { barArea, bar ->
                    bars[bar.uniqueId] = barArea
                    barDrawer.drawBar(
                        drawScope = this,
                        canvas = canvas,
                        barArea = barArea,
                        bar = bar,
                        selected = (bar.uniqueId == selectedUniqueId),
                        selectedBarColor = selectedBarColor
                    )
                }
            }
        }
    ) {
        /**
         *  Typically we could draw everything here, but because of the lack of canvas.drawText
         *  APIs we have to use Android's `nativeCanvas` which seems to be drawn behind
         *  Compose's canvas.
         */
        drawIntoCanvas { canvas ->
            val (xAxisArea, yAxisArea) = axisAreas(
                drawScope = this,
                totalSize = size,
                withYAxisLabels = barChartOptions.showYAxisLabels,
                xAxisDrawer = xAxisDrawer,
                labelDrawer = labelDrawer
            )
            val barDrawableArea = barDrawableArea(xAxisArea)

            if (barChartOptions.showXAxisLabels) {
                barChartData.forEachWithArea(
                    this,
                    barDrawableArea,
                    progress,
                    labelDrawer,
                    barChartOptions.barsSpacingFactor
                ) { barArea, bar ->
                    labelDrawer.drawLabel(
                        drawScope = this,
                        canvas = canvas,
                        label = bar.label,
                        barArea = barArea,
                        xAxisArea = xAxisArea
                    )
                }
            }

            if (barChartOptions.showYAxisLabels) {
                yAxisDrawer.drawAxisLabels(
                    drawScope = this,
                    canvas = canvas,
                    minValue = barChartData.minYValue,
                    maxValue = barChartData.maxYValue,
                    drawableArea = yAxisArea
                )
            }
        }
    }
}