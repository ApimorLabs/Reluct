package work.racka.reluct.android.screens.screentime.components

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.barChart.BarChartData
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.common.model.util.time.Week

internal suspend inline fun getWeeklyDeviceScreenTimeChartData(
    weeklyStats: ImmutableMap<Week, UsageStats>,
    barColor: Color
) = withContext(Dispatchers.IO) {
    persistentListOf<BarChartData.Bar>().builder().apply {
        for (key in weeklyStats.keys) {
            val data = weeklyStats.getValue(key)
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

internal suspend inline fun getWeeklyAppScreenTimeChartData(
    weeklyStats: ImmutableMap<Week, AppUsageStats>,
    barColor: Color
) = withContext(Dispatchers.IO) {
    persistentListOf<BarChartData.Bar>().builder().apply {
        for (key in weeklyStats.keys) {
            val data = weeklyStats.getValue(key)
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
