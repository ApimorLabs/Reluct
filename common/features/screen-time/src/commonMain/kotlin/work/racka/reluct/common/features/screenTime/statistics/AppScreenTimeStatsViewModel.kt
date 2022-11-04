package work.racka.reluct.common.features.screenTime.statistics

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.app_usage.GetAppUsageInfo
import work.racka.reluct.common.domain.usecases.limits.ManageAppTimeLimit
import work.racka.reluct.common.domain.usecases.limits.ManageDistractingApps
import work.racka.reluct.common.domain.usecases.limits.ManagePausedApps
import work.racka.reluct.common.domain.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.features.screenTime.statistics.states.ScreenTimeStatsEvents
import work.racka.reluct.common.features.screenTime.statistics.states.ScreenTimeStatsSelectedInfo
import work.racka.reluct.common.features.screenTime.statistics.states.appStats.AppScreenTimeStatsState
import work.racka.reluct.common.features.screenTime.statistics.states.appStats.AppSettingsState
import work.racka.reluct.common.features.screenTime.statistics.states.appStats.DailyAppUsageStatsState
import work.racka.reluct.common.features.screenTime.statistics.states.appStats.WeeklyAppUsageStatsState
import work.racka.reluct.common.features.screenTime.util.Constants
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.common.model.util.time.Week
import work.racka.reluct.common.model.util.time.WeekUtils

/**
 * Remember to pass the packageName in Koin `parametersOf` when getting an instance
 * of this class using Koin injection.
 */
class AppScreenTimeStatsViewModel(
    private val packageName: String,
    private val getAppUsageInfo: GetAppUsageInfo,
    private val manageAppTimeLimit: ManageAppTimeLimit,
    private val manageDistractingApps: ManageDistractingApps,
    private val managePausedApps: ManagePausedApps,
    private val getWeekRangeFromOffset: GetWeekRangeFromOffset
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
        selectedInfo,
        weeklyData,
        dailyData,
        appSettings
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
    val events: Flow<ScreenTimeStatsEvents> = _events.receiveAsFlow()

    private var weeklyDataJob: Job? = null
    private var appTimeLimitJob: Job? = null
    private var appSettingsJob: Job? = null

    init {
        initialize()
    }

    private fun initialize() {
        val todayIsoNumber = WeekUtils.currentDayOfWeek().isoDayNumber
        selectedInfo.update { it.copy(selectedDay = todayIsoNumber) }
        getData()
        loadAppSettings()
    }

    fun toggleDistractingState(value: Boolean) {
        vmScope.launch {
            if (value) {
                manageDistractingApps.markAsDistracting(packageName)
                _events.send(
                    ScreenTimeStatsEvents.ShowMessageDone(
                        true,
                        Constants.MARK_DISTRACTING
                    )
                )
            } else {
                manageDistractingApps.markAsNotDistracting(packageName)
                _events.send(
                    ScreenTimeStatsEvents.ShowMessageDone(
                        false,
                        Constants.UN_MARK_DISTRACTING
                    )
                )
            }
        }
    }

    fun togglePausedState(value: Boolean) {
        vmScope.launch {
            if (value) {
                managePausedApps.pauseApp(packageName)
                _events.send(
                    ScreenTimeStatsEvents.ShowMessageDone(
                        true,
                        Constants.MARK_PAUSED
                    )
                )
            } else {
                managePausedApps.unPauseApp(packageName)
                _events.send(
                    ScreenTimeStatsEvents.ShowMessageDone(
                        false,
                        Constants.UN_MARK_PAUSED
                    )
                )
            }
        }
    }

    fun saveTimeLimit(hours: Int, minutes: Int) {
        vmScope.launch {
            val appSettingsState = appSettings.value
            appTimeLimitJob?.cancel()
            if (appSettingsState is AppSettingsState.Data) {
                val newLimit = appSettingsState.appTimeLimit.copy(hours = hours, minutes = minutes)
                manageAppTimeLimit.setTimeLimit(newLimit)
                _events.send(ScreenTimeStatsEvents.TimeLimitChange(newLimit))
            }
        }
    }

    fun selectDay(selectedDayIsoNumber: Int) {
        selectedInfo.update { it.copy(selectedDay = selectedDayIsoNumber) }
        val currentWeekData = weeklyData.value
        getDailyData(selectedDayIsoNumber, currentWeekData.usageStats)
    }

    fun updateWeekOffset(weekOffsetValue: Int) {
        weeklyData.update {
            WeeklyAppUsageStatsState.Loading(weeklyUsageStats = it.usageStats)
        }
        selectedInfo.update { it.copy(weekOffset = weekOffsetValue) }
        weeklyDataJob?.cancel()
        getData()
    }

    private fun getData() {
        getWeeklyData()
    }

    private fun loadAppSettings() {
        appSettingsJob?.cancel()
        appSettings.update { AppSettingsState.Loading }
        appSettingsJob = vmScope.launch {
            manageAppTimeLimit.getLimit(packageName).collectLatest { app ->
                val isDistracting = manageDistractingApps.isDistractingApp(app.appInfo.packageName)
                val isPaused = managePausedApps.isPaused(app.appInfo.packageName)
                appSettings.update {
                    AppSettingsState.Data(
                        appTimeLimit = app,
                        isDistractingApp = isDistracting,
                        isPaused = isPaused
                    )
                }
            }
        }
    }

    private fun getWeeklyData() {
        weeklyDataJob?.cancel()
        weeklyData.update { WeeklyAppUsageStatsState.Loading() }
        weeklyDataJob = vmScope.launch {
            val weekOffset = selectedInfo.value.weekOffset
            val selectedDay = selectedInfo.value.selectedDay
            val weekOffsetText = getWeekRangeFromOffset.invoke(weekOffset)
            selectedInfo.update { it.copy(selectedWeekText = weekOffsetText) }
            val weekData = getAppUsageInfo.weeklyUsage(
                weekOffset = weekOffset,
                packageName = packageName
            )
            if (weekData.isEmpty()) {
                weeklyData.update { WeeklyAppUsageStatsState.Empty }
            } else {
                val totalTimeMillis = weekData.values.sumOf { it.appUsageInfo.timeInForeground }
                val formattedTime = TimeUtils.getFormattedTimeDurationString(totalTimeMillis)
                weeklyData.update {
                    WeeklyAppUsageStatsState.Data(
                        weeklyUsageStats = weekData,
                        weeklyFormattedTotalTime = formattedTime
                    )
                }
            }

            getDailyData(selectedDay, weekData)
        }
    }

    private fun getDailyData(selectedDayIso: Int, weekData: Map<Week, AppUsageStats>) {
        println("Selected Day: $selectedDayIso")
        val dayData = weekData.getValue(getWeek(selectedDayIso))
        dailyData.update {
            DailyAppUsageStatsState.Data(
                usageStat = dayData
            )
        }
    }

    private fun getWeek(dayIsoNumber: Int) = if (dayIsoNumber <= 0) {
        Week.MONDAY
    } else {
        Week.values()[dayIsoNumber - 1]
    }
}
