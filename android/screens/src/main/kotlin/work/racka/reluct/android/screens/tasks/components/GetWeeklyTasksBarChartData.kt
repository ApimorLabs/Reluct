package work.racka.reluct.android.screens.tasks.components

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.android.compose.components.cards.statistics.ChartData
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week
import work.racka.reluct.compose.common.charts.barChart.BarChartData

@Composable
internal fun getWeeklyTasksBarChartData(
    weeklyTasksProvider: () -> ImmutableMap<Week, DailyTasksStats>,
    isLoadingProvider: () -> Boolean,
    barColor: Color
): State<ChartData<BarChartData.Bar>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val weeklyStats = remember { derivedStateOf { weeklyTasksProvider() } }
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
                            value = data?.completedTasksCount?.toFloat() ?: 0f,
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
