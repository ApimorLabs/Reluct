package work.racka.reluct.android.compose.components.cards.statistics.tasks

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsBarChartCard
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.barChart.BarChartData
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

@Composable
fun TasksStatisticsCard(
    modifier: Modifier = Modifier,
    barChartState: StatisticsChartState<Map<Week, DailyTasksStats>>,
    barColor: Color = MaterialTheme.colorScheme.secondary,
    selectedDayText: String,
    selectedDayTasksDone: Int,
    selectedDayTasksPending: Int,
    totalWeekTaskCount: Int,
    selectedDayIsoNumber: Int,
    onBarClicked: (Int) -> Unit,
    weekUpdateButton: @Composable () -> Unit,
) {
    val bars = remember(barChartState) {
        derivedStateOf {
            val tempList = mutableListOf<BarChartData.Bar>()
            barChartState.data.forEach { entry ->
                BarChartData.Bar(
                    value = entry.value.completedTasksCount.toFloat(),
                    color = barColor,
                    label = entry.key.dayAcronym,
                    uniqueId = entry.key.isoDayNumber
                )
            }
            tempList
        }
    }

    StatisticsBarChartCard(
        modifier = modifier,
        bars = bars.value,
        dataLoading = barChartState is StatisticsChartState.Loading,
        selectedDayIsoNumber = selectedDayIsoNumber,
        onBarClicked = { onBarClicked(it) },
        topLeftText = {
            Text(
                text = selectedDayText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current
            )
        },
        topRightText = {
            Text(
                text = stringResource(R.string.tasks_tally_text_arg,
                    selectedDayTasksDone,
                    selectedDayTasksPending),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current
            )
        },
        belowChartText = {
            Text(
                text = stringResource(R.string.weekly_task_count_arg, totalWeekTaskCount),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current
            )
        },
        bodyContent = weekUpdateButton
    )
}