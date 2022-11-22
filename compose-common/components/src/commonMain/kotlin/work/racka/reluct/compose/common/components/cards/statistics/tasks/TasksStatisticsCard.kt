package work.racka.reluct.compose.common.components.cards.statistics.tasks

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
fun TasksStatisticsCard(
    chartData: State<ChartData<BarChartData.Bar>>,
    selectedDayText: () -> String,
    selectedDayTasksDone: () -> Int,
    selectedDayTasksPending: () -> Int,
    totalWeekTaskCount: () -> Int,
    selectedDayIsoNumber: () -> Int,
    onBarClicked: (Int) -> Unit,
    weekUpdateButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatisticsBarChartCard(
        modifier = modifier.animateContentSize(),
        bars = chartData.value.data,
        selectedBarColor = MaterialTheme.colorScheme.primary,
        dataLoading = chartData.value.isLoading,
        noDataText = stringResource(SharedRes.strings.no_completed_tasks_text),
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
                text = stringResource(
                    SharedRes.strings.tasks_tally_text_arg,
                    selectedDayTasksDone(),
                    selectedDayTasksPending() + selectedDayTasksDone()
                ),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        belowChartText = {
            Spacer(modifier = Modifier)
            Text(
                text = stringResource(
                    SharedRes.strings.weekly_task_count_arg,
                    totalWeekTaskCount()
                ),
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
