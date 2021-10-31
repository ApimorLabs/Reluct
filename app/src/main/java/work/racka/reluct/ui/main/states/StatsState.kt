package work.racka.reluct.ui.main.states

import work.racka.reluct.data.local.usagestats.Week
import work.racka.reluct.model.UsageStats

sealed class StatsState {
    data class Stats(
        val usageStats: List<UsageStats> = listOf(),
        val dayStats: UsageStats,
        val selectedWeekOffset: Int = 0,
        val selectedDay: Int = 0
    ) : StatsState()

    companion object {
        val EmptyStats = Stats(
            dayStats = UsageStats(
                listOf(),
                Week.MONDAY,
                "",
                0L
            )
        )
    }
}
