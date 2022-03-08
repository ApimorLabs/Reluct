package work.racka.reluct.ui.components.summary

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import work.racka.reluct.barChart.BarChart
import work.racka.reluct.barChart.BarChartData
import work.racka.reluct.barChart.BarChartOptions
import work.racka.reluct.barChart.renderer.label.SimpleValueDrawer
import work.racka.reluct.barChart.renderer.xaxis.SimpleXAxisDrawer
import work.racka.reluct.barChart.renderer.yaxis.SimpleYAxisDrawer
import work.racka.reluct.ui.main.states.StatsState
import work.racka.reluct.ui.theme.Dimens
import java.util.concurrent.TimeUnit

@Composable
fun DataPieChart(
    modifier: Modifier = Modifier,
    stats: StatsState.Stats,
    onBarClicked: (Int) -> Unit = { }
) {

    ScreenTimePieChart(
        modifier = modifier,
        stats = stats,
        onBarClicked = {
            onBarClicked(it)
        }
    )
}

@Composable
fun ScreenTimePieChart(
    modifier: Modifier = Modifier,
    stats: StatsState.Stats,
    onBarClicked: (Int) -> Unit = { }
) {

    val list = mutableListOf<BarChartData.Bar>()
    stats.usageStats.forEach { stats ->
        list.add(
            BarChartData.Bar(
                value = stats.totalScreenTime.toFloat(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                label = stats.dayOfWeek.day,
                uniqueId = stats.dayOfWeek.value
            )
        )
    }
    BarChart(
        barChartData = BarChartData(
            bars = list
        ),
        modifier = modifier
            .fillMaxWidth()
            .size(size = 160.dp)
            .padding(Dimens.MediumPadding.size),
        selectedUniqueId = stats.selectedDay,
        selectedBarColor = MaterialTheme.colorScheme.primary,
        onBarClicked = {
            onBarClicked(it)
        },
        xAxisDrawer = SimpleXAxisDrawer(
            axisLineColor = MaterialTheme.colorScheme.onBackground
                .copy(alpha = 0.5f)
        ),
        yAxisDrawer = SimpleYAxisDrawer(
            labelValueFormatter = { value ->
                val hr = TimeUnit.MILLISECONDS.toHours(value.toLong())
                "$hr h"
            },
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
        }
    )
}