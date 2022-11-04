package work.racka.reluct.common.features.screenTime.statistics.states

data class ScreenTimeStatsSelectedInfo(
    val weekOffset: Int = 0,
    val selectedWeekText: String = "...",
    val selectedDay: Int = 0
)
