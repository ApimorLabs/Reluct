package work.racka.reluct.android.compose.components.cards.statistics.screen_time

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsBarChartCard
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.barChart.BarChartData
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.Week

@Composable
fun AppScreenTimeStatisticsCard(
    modifier: Modifier = Modifier,
    barChartState: StatisticsChartState<List<AppUsageStats>>,
    barColor: Color = MaterialTheme.colorScheme.secondary
        .copy(alpha = .7f),
    selectedDayText: String,
    selectedDayScreenTime: String,
    weeklyTotalScreenTime: String,
    selectedDayIsoNumber: Int,
    onBarClicked: (Int) -> Unit,
    weekUpdateButton: @Composable () -> Unit,
) {

    val bars = remember(barChartState) {
        derivedStateOf {
            val tempList = mutableListOf<BarChartData.Bar>()
            barChartState.data.forEach { entry ->
                tempList.add(
                    BarChartData.Bar(
                        value = entry.appUsageInfo.timeInForeground.toFloat(),
                        color = barColor,
                        label = getWeek(entry.dayIsoNumber).dayAcronym,
                        uniqueId = entry.dayIsoNumber
                    )
                )
            }
            tempList.toList()
        }
    }

    StatisticsBarChartCard(
        modifier = modifier,
        bars = bars.value,
        dataLoading = barChartState is StatisticsChartState.Loading,
        noDataText = stringResource(id = R.string.no_app_usage_data_text),
        selectedBarColor = MaterialTheme.colorScheme.primary,
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
                text = selectedDayScreenTime,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        belowChartText = {
            Spacer(modifier = Modifier)
            Text(
                text = if (barChartState is StatisticsChartState.Success) {
                    stringResource(R.string.weekly_screen_time_tally, weeklyTotalScreenTime)
                } else "• • • • •",
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

private fun getWeek(isoDayNumber: Int) =
    Week.values().firstOrNull { it.isoDayNumber == isoDayNumber } ?: Week.MONDAY