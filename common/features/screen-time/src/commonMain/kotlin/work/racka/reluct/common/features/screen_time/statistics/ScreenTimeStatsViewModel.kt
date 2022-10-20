package work.racka.reluct.common.features.screen_time.statistics

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.app_usage.GetUsageStats
import work.racka.reluct.common.domain.usecases.limits.ManageAppTimeLimit
import work.racka.reluct.common.domain.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.features.screen_time.limits.states.AppTimeLimitState
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsEvents
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsSelectedInfo
import work.racka.reluct.common.features.screen_time.statistics.states.all_stats.DailyUsageStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.all_stats.ScreenTimeStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.all_stats.WeeklyUsageStatsState
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.common.model.util.time.WeekUtils

class ScreenTimeStatsViewModel(
    private val getUsageStats: GetUsageStats,
    private val getWeekRangeFromOffset: GetWeekRangeFromOffset,
    private val manageAppTimeLimit: ManageAppTimeLimit
) : CommonViewModel() {

    private val selectedInfo: MutableStateFlow<ScreenTimeStatsSelectedInfo> =
        MutableStateFlow(ScreenTimeStatsSelectedInfo())
    private val weeklyUsageStatsState: MutableStateFlow<WeeklyUsageStatsState> =
        MutableStateFlow(WeeklyUsageStatsState.Empty)
    private val dailyUsageStatsState: MutableStateFlow<DailyUsageStatsState> =
        MutableStateFlow(DailyUsageStatsState.Empty)
    private val appTimeLimitState: MutableStateFlow<AppTimeLimitState> =
        MutableStateFlow(AppTimeLimitState.Nothing)

    private val isGranted = MutableStateFlow(false)

    val uiState: StateFlow<ScreenTimeStatsState> = combine(
        selectedInfo, weeklyUsageStatsState, dailyUsageStatsState, appTimeLimitState
    ) { selectedInfo, weeklyUsageStatsState, dailyUsageStatsState, appTimeLimitState ->
        ScreenTimeStatsState(
            selectedInfo = selectedInfo,
            weeklyData = weeklyUsageStatsState,
            dailyData = dailyUsageStatsState,
            appTimeLimit = appTimeLimitState
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScreenTimeStatsState()
    )

    private val _events = Channel<ScreenTimeStatsEvents>(capacity = Channel.UNLIMITED)
    val events: Flow<ScreenTimeStatsEvents>
        get() = _events.receiveAsFlow()

    private var dailyScreenTimeStatsJob: Job? = null
    private var weeklyScreenTimeStatsJob: Job? = null
    private var getDataJob: Job? = null
    private var appTimeLimitJob: Job? = null

    init {
        val todayIsoNumber = WeekUtils.currentDayOfWeek().isoDayNumber
        selectedInfo.update { it.copy(selectedDay = todayIsoNumber) }
        getData()
    }

    private fun getData() {
        getDataJob = vmScope.launch {
            isGranted.collectLatest { granted ->
                if (granted) {
                    getWeeklyData()
                    getDailyData()
                }
            }
        }
    }

    private fun getDailyData() {
        dailyUsageStatsState.update { DailyUsageStatsState.Loading(dailyUsageStats = it.usageStat) }
        dailyScreenTimeStatsJob = vmScope.launch {
            val selected = selectedInfo.value
            val dailyData = getUsageStats.dailyUsage(
                weekOffset = selected.weekOffset,
                dayIsoNumber = selected.selectedDay
            )
            if (dailyData.appsUsageList.isEmpty()) {
                dailyUsageStatsState.update { DailyUsageStatsState.Empty }
            } else {
                dailyUsageStatsState.update {
                    DailyUsageStatsState.Data(
                        dailyUsageStats = dailyData
                    )
                }
            }
        }
    }

    private fun getWeeklyData() {
        weeklyUsageStatsState.update { WeeklyUsageStatsState.Loading() }
        weeklyScreenTimeStatsJob = vmScope.launch {
            val weekOffset = selectedInfo.value.weekOffset
            val weekOffsetText = getWeekRangeFromOffset.invoke(weekOffset)
            selectedInfo.update { it.copy(selectedWeekText = weekOffsetText) }
            val weeklyData = getUsageStats.weeklyUsage(weekOffset = weekOffset)
            if (weeklyData.isEmpty()) {
                weeklyUsageStatsState.update { WeeklyUsageStatsState.Empty }
            } else {
                val totalWeeklyTimeInMillis = weeklyData.values.sumOf { it.totalScreenTime }
                val formattedTime = TimeUtils
                    .getFormattedTimeDurationString(totalWeeklyTimeInMillis)
                weeklyUsageStatsState.update {
                    WeeklyUsageStatsState.Data(
                        weeklyUsageStats = weeklyData,
                        weeklyFormattedTotalTime = formattedTime
                    )
                }
            }
        }
    }

    fun permissionCheck(isGranted: Boolean) {
        this.isGranted.update { isGranted }
    }

    fun selectAppTimeLimit(packageName: String) {
        appTimeLimitJob?.cancel()
        appTimeLimitJob = vmScope.launch {
            appTimeLimitState.update { AppTimeLimitState.Loading }
            val appTimeLimit = manageAppTimeLimit.getSync(packageName)
            appTimeLimitState.update { AppTimeLimitState.Data(timeLimit = appTimeLimit) }
        }
    }

    fun saveTimeLimit(hours: Int, minutes: Int) {
        vmScope.launch {
            val limitState = appTimeLimitState.value
            appTimeLimitJob?.cancel()
            if (limitState is AppTimeLimitState.Data) {
                val newLimit = limitState.timeLimit.copy(hours = hours, minutes = minutes)
                manageAppTimeLimit.setTimeLimit(newLimit)
                _events.send(ScreenTimeStatsEvents.TimeLimitChange(newLimit))
            }
        }
    }

    fun selectDay(selectedDayIsoNumber: Int) {
        dailyUsageStatsState.update { DailyUsageStatsState.Loading(it.usageStat) }
        selectedInfo.update { it.copy(selectedDay = selectedDayIsoNumber) }
        dailyScreenTimeStatsJob?.cancel()
        getDailyData()
    }

    fun updateWeekOffset(weekOffsetValue: Int) {
        weeklyUsageStatsState.update {
            WeeklyUsageStatsState.Loading(weeklyUsageStats = it.usageStats)
        }
        selectedInfo.update { it.copy(weekOffset = weekOffsetValue) }
        dailyScreenTimeStatsJob?.cancel()
        weeklyScreenTimeStatsJob?.cancel()
        getDataJob?.cancel()
        getData()
    }

    fun navigateToAppInfo(packageName: String) {
        _events.trySend(
            ScreenTimeStatsEvents.Navigation.NavigateToAppInfo(packageName)
        )
    }

    fun openAppTimerSettings(packageName: String) {
        _events.trySend(
            ScreenTimeStatsEvents.Navigation.OpenAppTimerSettings(packageName)
        )
    }

    override fun destroy() {
        dailyScreenTimeStatsJob?.cancel()
        weeklyScreenTimeStatsJob?.cancel()
        getDataJob?.cancel()
        super.destroy()
    }
}