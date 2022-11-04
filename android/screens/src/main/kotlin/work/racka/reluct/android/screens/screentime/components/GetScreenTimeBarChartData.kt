package work.racka.reluct.android.screens.screentime.components

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.android.compose.components.cards.statistics.ChartData
import work.racka.reluct.barChart.BarChartData
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week

@Composable
internal fun getWeeklyDeviceScreenTimeChartData(
    weeklyStatsProvider: () -> ImmutableMap<Week, UsageStats>,
    isLoadingProvider: () -> Boolean,
    barColor: Color
): State<ChartData<BarChartData.Bar>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyStatsProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        weeklyStats.value
    ) {
        val data = withContext(Dispatchers.IO) {
            persistentListOf<BarChartData.Bar>().builder().apply {
                for (key in weeklyStats.value.keys) {
                    val data = weeklyStats.value.getValue(key)
                    add(
                        BarChartData.Bar(
                            value = data.totalScreenTime.toFloat(),
                            color = barColor,
                            label = key.dayAcronym,
                            uniqueId = key.isoDayNumber
                        )
                    )
                }
            }.build().toImmutableList()
        }
        value = ChartData(data = data, isLoading = isLoading.value)
    }
}

@Composable
internal fun getWeeklyAppScreenTimeChartData(
    weeklyStatsProvider: () -> ImmutableMap<Week, AppUsageStats>,
    isLoadingProvider: () -> Boolean,
    barColor: Color,
): State<ChartData<BarChartData.Bar>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyStatsProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        weeklyStats.value
    ) {
        val data = withContext(Dispatchers.IO) {
            persistentListOf<BarChartData.Bar>().builder().apply {
                for (key in weeklyStats.value.keys) {
                    val data = weeklyStats.value.getValue(key)
                    add(
                        BarChartData.Bar(
                            value = data.appUsageInfo.timeInForeground.toFloat(),
                            color = barColor,
                            label = key.dayAcronym,
                            uniqueId = key.isoDayNumber
                        )
                    )
                }
            }.build().toImmutableList()
        }
        value = ChartData(data = data, isLoading = isLoading.value)
    }
}
