package work.racka.reluct.ui.components.summary

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import work.racka.reluct.model.UsageStats
import work.racka.reluct.ui.components.charts.pieChart.PieChart
import work.racka.reluct.ui.components.charts.pieChart.PieChartData
import work.racka.reluct.ui.components.charts.pieChart.renderer.slice.SimpleSliceDrawer
import work.racka.reluct.ui.components.charts.pieChart.renderer.text.SimpleTextDrawer
import work.racka.reluct.utils.Utils
import java.util.concurrent.TimeUnit

@Composable
fun SummaryPieChart(
    modifier: Modifier = Modifier,
    dayStats: UsageStats
) {

    val slices = remember(dayStats) {
        mutableStateOf(mutableListOf<PieChartData.Slice>())
    }
    var otherTime = 0L
    dayStats.appsUsageList.forEach { appUsageInfo ->
        if (appUsageInfo.timeInForeground >=
            TimeUnit.MINUTES.toMillis(15L)
        ) {
            slices.value.add(
                PieChartData.Slice(
                    value = appUsageInfo.timeInForeground.toFloat(),
                    color = Color(appUsageInfo.dominantColor)
                )
            )
        } else {
            otherTime += appUsageInfo.timeInForeground
        }
    }

    slices.value.add(
        PieChartData.Slice(otherTime.toFloat(), Color.Gray)
    )

    PieChart(
        pieChartData = PieChartData(slices.value, 0.05f),
        modifier = modifier.size(size = 200.dp),
        sliceDrawer = SimpleSliceDrawer(10f),
        centerTextDrawer = SimpleTextDrawer(
            labelTextColor = MaterialTheme.colorScheme.onBackground
        ),
        centerText = Utils.getFormattedTime(
            dayStats.totalScreenTime
        )
    )
}