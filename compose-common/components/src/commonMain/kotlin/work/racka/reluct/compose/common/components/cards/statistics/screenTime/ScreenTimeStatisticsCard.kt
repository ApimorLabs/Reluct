package work.racka.reluct.compose.common.components.cards.statistics.screenTime

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.compose.common.charts.barChart.BarChartData
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.cards.statistics.ChartData
import work.racka.reluct.compose.common.components.cards.statistics.StatisticsBarChartCard
import work.racka.reluct.compose.common.components.resources.stringResource

@Composable
fun ScreenTimeStatisticsCard(
    chartData: State<ChartData<BarChartData.Bar>>,
    selectedDayText: () -> String,
    selectedDayScreenTime: () -> String,
    weeklyTotalScreenTime: () -> String,
    selectedDayIsoNumber: () -> Int,
    onBarClicked: (Int) -> Unit,
    weekUpdateButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatisticsBarChartCard(
        modifier = modifier.animateContentSize(),
        bars = chartData.value.data,
        dataLoading = chartData.value.isLoading,
        noDataText = stringResource(SharedRes.strings.no_app_usage_data_text),
        selectedBarColor = MaterialTheme.colorScheme.primary,
        selectedDayIsoNumber = selectedDayIsoNumber(),
        onBarClicked = { onBarClicked(it) },
        topLeftText = {
            Text(
                text = selectedDayText(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current
            )
        },
        topRightText = {
            Text(
                text = selectedDayScreenTime(),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        belowChartText = {
            Spacer(modifier = Modifier)
            Text(
                text = if (!chartData.value.isLoading) {
                    stringResource(
                        SharedRes.strings.weekly_screen_time_tally,
                        weeklyTotalScreenTime()
                    )
                } else {
                    "• • • • •"
                },
                style = MaterialTheme.typography.titleLarge
                    .copy(fontWeight = FontWeight.Medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current
            )
        },
        bodyContent = weekUpdateButton
    )
}
