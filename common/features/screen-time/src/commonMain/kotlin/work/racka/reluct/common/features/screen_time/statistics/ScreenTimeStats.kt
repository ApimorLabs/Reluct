package work.racka.reluct.common.features.screen_time.statistics

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsEvents
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsState

interface ScreenTimeStats {
    val uiState: StateFlow<ScreenTimeStatsState>
    val events: Flow<ScreenTimeStatsEvents>
    fun permissionCheck(isGranted: Boolean = false)
    fun selectDay(selectedDayIsoNumber: Int)
    fun updateWeekOffset(weekOffsetValue: Int)
    fun navigateToAppInfo(packageName: String)
    fun openAppTimerSettings(packageName: String)
}