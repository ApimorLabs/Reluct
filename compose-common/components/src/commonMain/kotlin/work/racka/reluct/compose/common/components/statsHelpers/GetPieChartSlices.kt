package work.racka.reluct.compose.common.components.statsHelpers

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.withContext
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo
import work.racka.reluct.compose.common.charts.pieChart.PieChartData
import work.racka.reluct.compose.common.components.cards.statistics.ChartData
import work.racka.reluct.compose.common.components.util.extractColor

@Composable
internal fun getScreenTimePieChartData(
    appsUsageProvider: () -> ImmutableList<AppUsageInfo>,
    isLoadingProvider: () -> Boolean
): State<ChartData<PieChartData.Slice>> {
    val isLoading = remember { derivedStateOf { isLoadingProvider() } }
    val appsUsage = remember { derivedStateOf { appsUsageProvider() } }
    return produceState(
        initialValue = ChartData(isLoading = isLoading.value),
        isLoading.value,
        appsUsage.value,
    ) {
        val data = withContext(StatsDispatcher.Dispatcher) {
            persistentListOf<PieChartData.Slice>().builder().apply {
                val firstItems = appsUsage.value.take(4)
                val otherItems = appsUsage.value - firstItems.toSet()
                val otherSlice = PieChartData.Slice(
                    value = otherItems.sumOf { it.timeInForeground }.toFloat(),
                    color = Color.Gray
                )
                for (i in firstItems.indices) {
                    val data = firstItems[i]
                    val slice = PieChartData.Slice(
                        value = data.timeInForeground.toFloat(),
                        color = data.appIcon.extractColor()
                    )
                    add(slice)
                }
                add(otherSlice)
            }.build().toImmutableList()
        }
        value = ChartData(data = data, isLoading = isLoading.value)
    }
}
