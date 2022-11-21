package work.racka.reluct.android.compose.components.cards.statistics

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.compose.common.charts.barChart.BarChart
import work.racka.reluct.compose.common.charts.barChart.BarChartData
import work.racka.reluct.compose.common.charts.barChart.BarChartOptions
import work.racka.reluct.compose.common.charts.barChart.renderer.label.SimpleValueDrawer
import work.racka.reluct.compose.common.charts.barChart.renderer.xaxis.SimpleXAxisDrawer
import work.racka.reluct.compose.common.charts.barChart.renderer.yaxis.SimpleYAxisDrawer

@Composable
internal fun StatisticsBarChartBase(
    bars: ImmutableList<BarChartData.Bar>,
    selectedDayIsoNumber: Int,
    onBarClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedBarColor: Color = MaterialTheme.colorScheme.primary,
) {
    BarChart(
        barChartData = BarChartData(bars = bars),
        modifier = modifier.fillMaxWidth(),
        selectedUniqueId = selectedDayIsoNumber,
        selectedBarColor = selectedBarColor,
        onBarClicked = {
            onBarClicked(it)
        },
        xAxisDrawer = SimpleXAxisDrawer(
            axisLineColor = MaterialTheme.colorScheme.onBackground
                .copy(alpha = 0.5f)
        ),
        yAxisDrawer = SimpleYAxisDrawer(
            labelTextColor = MaterialTheme.colorScheme.onBackground
                .copy(alpha = 0.5f),
            axisLineColor = MaterialTheme.colorScheme.onBackground
                .copy(alpha = 0.5f)
        ),
        labelDrawer = SimpleValueDrawer(
            labelTextColor = MaterialTheme.colorScheme.onBackground
                .copy(alpha = 0.5f)
        ),
        barChartOptions = BarChartOptions().apply {
            barsSpacingFactor = 0.05f
            showIntervalLines = false
            showYAxisLabels = false
        }
    )
}
