package work.racka.reluct.common.features.screen_time.statistics

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.app_usage.GetWeeklyAppUsageInfo
import work.racka.reluct.common.data.usecases.limits.ManageAppTimeLimit
import work.racka.reluct.common.data.usecases.limits.ManageDistractingApps
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsEvents
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsSelectedInfo
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.AppScreenTimeStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.AppSettingsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.DailyAppUsageStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.WeeklyAppUsageStatsState

class AppScreenTimeStatsViewModel(
    private val getWeeklyAppUsage: GetWeeklyAppUsageInfo,
    private val manageAppTimeLimit: ManageAppTimeLimit,
    private val manageDistractingApps: ManageDistractingApps
) : CommonViewModel() {

    private val selectedInfo: MutableStateFlow<ScreenTimeStatsSelectedInfo> =
        MutableStateFlow(ScreenTimeStatsSelectedInfo())
    private val weeklyData: MutableStateFlow<WeeklyAppUsageStatsState> =
        MutableStateFlow(WeeklyAppUsageStatsState.Empty)
    private val dailyData: MutableStateFlow<DailyAppUsageStatsState> =
        MutableStateFlow(DailyAppUsageStatsState.Empty)
    private val appSettings: MutableStateFlow<AppSettingsState> =
        MutableStateFlow(AppSettingsState.Nothing)

    val uiState: StateFlow<AppScreenTimeStatsState> = combine(
        selectedInfo, weeklyData, dailyData, appSettings
    ) { selectedInfo, weeklyData, dailyData, appSettings ->
        AppScreenTimeStatsState(
            selectedInfo = selectedInfo,
            weeklyData = weeklyData,
            dailyData = dailyData,
            appSettingsState = appSettings
        )
    }.stateIn(
        scope = vmScope,
        initialValue = AppScreenTimeStatsState(),
        started = SharingStarted.WhileSubscribed(5_000L)
    )

    private val _events = Channel<ScreenTimeStatsEvents>(capacity = Channel.UNLIMITED)
    private val events: Flow<ScreenTimeStatsEvents> = _events.receiveAsFlow()
}