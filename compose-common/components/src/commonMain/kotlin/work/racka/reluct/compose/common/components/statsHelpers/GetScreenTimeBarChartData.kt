package work.racka.reluct.compose.common.components.statsHelpers

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week
import work.racka.reluct.compose.common.charts.barChart.BarChartData
import work.racka.reluct.compose.common.components.cards.statistics.ChartData

@Composable
fun getWeeklyDeviceScreenTimeChartData(
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
                    val data = weeklyStats.value[key]
                    add(
                        BarChartData.Bar(
                            value = data?.totalScreenTime?.toFloat() ?: 0f,
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
fun getWeeklyAppScreenTimeChartData(
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
                    val data = weeklyStats.value[key]
                    add(
                        BarChartData.Bar(
                            value = data?.appUsageInfo?.timeInForeground?.toFloat() ?: 0f,
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
