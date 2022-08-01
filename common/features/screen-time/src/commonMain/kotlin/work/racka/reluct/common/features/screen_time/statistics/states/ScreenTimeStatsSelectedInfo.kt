package work.racka.reluct.common.features.screen_time.statistics.states

data class ScreenTimeStatsSelectedInfo(
    val weekOffset: Int = 0,
    val selectedWeekText: String = "...",
    val selectedDay: Int = 0
)