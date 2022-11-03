package work.racka.reluct.android.screens.tasks.components

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.barChart.BarChartData
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

internal suspend inline fun getTasksBarChartBars(
    weeklyTasks: ImmutableMap<Week, DailyTasksStats>,
    barColor: Color
) = withContext(Dispatchers.IO) {
    persistentListOf<BarChartData.Bar>().builder().apply {
        for (key in weeklyTasks.keys) {
            val data = weeklyTasks.getValue(key)
            add(
                BarChartData.Bar(
                    value = data.completedTasksCount.toFloat(),
                    color = barColor,
                    label = key.dayAcronym,
                    uniqueId = key.isoDayNumber
                )
            )
        }
    }.build().toImmutableList()
}