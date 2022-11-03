package work.racka.reluct.android.screens.dashboard.components

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.android.compose.components.util.extractColor
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo
import work.racka.reluct.pieChart.PieChartData

internal suspend inline fun getPieChartSlices(appsUsage: ImmutableList<AppUsageInfo>) =
    withContext(Dispatchers.IO) {
        persistentListOf<PieChartData.Slice>().builder().apply {
            val firstItems = appsUsage.take(4)
            val otherItems = appsUsage - firstItems.toSet()
            val otherSlice = PieChartData.Slice(
                value = otherItems.sumOf { it.timeInForeground }.toFloat(),
                color = Color.Gray
            )
            for (i in firstItems.indices) {
                val data = firstItems[i]
                val colorInt = data.appIcon.icon.extractColor()
                val slice = PieChartData.Slice(
                    value = data.timeInForeground.toFloat(),
                    color = Color(colorInt)
                )
                add(slice)
            }
            add(otherSlice)
        }.build().toImmutableList()
    }
