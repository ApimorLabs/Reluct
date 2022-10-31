package work.racka.reluct.android.compose.components.cards.statistics.screen_time

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsBarChartCard
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.barChart.BarChartData
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.Week

@Composable
fun AppScreenTimeStatisticsCard(
    barChartState: StatisticsChartState<ImmutableMap<Week, AppUsageStats>>,
    selectedDayText: String,
    selectedDayScreenTime: String,
    weeklyTotalScreenTime: String,
    selectedDayIsoNumber: Int,
    onBarClicked: (Int) -> Unit,
    weekUpdateButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.secondary
        .copy(alpha = .7f),
) {
    val bars by remember(barChartState.data) {
        derivedStateOf {
            persistentListOf<BarChartData.Bar>().builder().apply {
                barChartState.data.forEach { entry ->
                    add(
                        BarChartData.Bar(
                            value = entry.value.appUsageInfo.timeInForeground.toFloat(),
                            color = barColor,
                            label = entry.key.dayAcronym,
                            uniqueId = entry.key.isoDayNumber
                        )
                    )
                }
            }.build().toImmutableList()
        }
    }

    StatisticsBarChartCard(
        modifier = modifier,
        bars = bars,
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

private fun getWeek(isoDayNumber: Int) =
    Week.values().firstOrNull { it.isoDayNumber == isoDayNumber } ?: Week.MONDAY
