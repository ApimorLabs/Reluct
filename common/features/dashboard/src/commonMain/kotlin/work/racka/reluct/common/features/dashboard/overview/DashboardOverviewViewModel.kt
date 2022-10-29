package work.racka.reluct.common.features.dashboard.overview

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.domain.usecases.app_usage.GetUsageStats
import work.racka.reluct.common.domain.usecases.goals.GetGoals
import work.racka.reluct.common.domain.usecases.goals.ModifyGoals
import work.racka.reluct.common.domain.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.domain.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.dashboard.overview.states.DashboardEvents
import work.racka.reluct.common.features.dashboard.overview.states.DashboardOverviewState
import work.racka.reluct.common.features.dashboard.overview.states.TodayScreenTimeState
import work.racka.reluct.common.features.dashboard.overview.states.TodayTasksState
import work.racka.reluct.common.features.screen_time.services.ScreenTimeServices
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.time.WeekUtils

class DashboardOverviewViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val getUsageStats: GetUsageStats,
    private val getGoals: GetGoals,
    private val modifyGoals: ModifyGoals,
    private val screenTimeServices: ScreenTimeServices
) : CommonViewModel() {

    private val todayScreenTimeState: MutableStateFlow<TodayScreenTimeState> =
        MutableStateFlow(TodayScreenTimeState.Nothing)
    private val todayTasksState: MutableStateFlow<TodayTasksState> =
        MutableStateFlow(TodayTasksState.Nothing)
    private val goals: MutableStateFlow<List<Goal>> = MutableStateFlow(listOf())

    val uiState: StateFlow<DashboardOverviewState> = combine(
        todayScreenTimeState, todayTasksState, goals
    ) { todayScreenTimeState, todayTasksState, goals ->
        DashboardOverviewState(
            todayScreenTimeState = todayScreenTimeState,
            todayTasksState = todayTasksState,
            goals = goals
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardOverviewState()
    )

    private val _events = Channel<DashboardEvents>(Channel.UNLIMITED)
    val events: Flow<DashboardEvents> = _events.receiveAsFlow()

    private var dailyScreenTimeJob: Job? = null
    private var permissionsJob: Job? = null
    private var goalsJob: Job? = null
    private var pendingTasksJob: Job? = null

    private val permissionGranted = MutableStateFlow(false)

    init {

    }

    fun initializeData() {
        permissionsJob ?: run { initializeScreenTimeData() }
        pendingTasksJob ?: run { getPendingTasks() }
        goalsJob ?: run { initializeGoals() }
    }

    fun permissionCheck(isGranted: Boolean) {
        permissionGranted.update { isGranted }
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        vmScope.launch {
            modifyTasksUsesCase.toggleTaskDone(task, isDone)
            _events.send(DashboardEvents.ShowMessageDone(isDone, task.title))
        }
    }

    private fun getPendingTasks() {
        pendingTasksJob?.cancel()
        todayTasksState.update { TodayTasksState.Loading(it.pending) }
        pendingTasksJob = vmScope.launch {
            // We get only 5 pending Tasks
            getTasksUseCase.getPendingTasks(factor = 1L, limitBy = 5).collectLatest { tasks ->
                todayTasksState.update { TodayTasksState.Data(tasks = tasks) }
            }
        }
    }

    private fun initializeScreenTimeData() {
        permissionsJob?.cancel()
        permissionsJob = vmScope.launch {
            permissionGranted.collectLatest { isGranted ->
                if (isGranted) {
                    screenTimeServices.startLimitsService()
                    getDailyScreenTime()
                }
            }
        }
    }

    private fun getDailyScreenTime() {
        dailyScreenTimeJob?.cancel()
        todayScreenTimeState.update { TodayScreenTimeState.Loading(usageStats = it.usageStats) }
        dailyScreenTimeJob = vmScope.launch {
            val today = WeekUtils.currentDayOfWeek()
            val dailyData = getUsageStats.dailyUsage(dayIsoNumber = today.isoDayNumber)
            todayScreenTimeState.update {
                TodayScreenTimeState.Data(
                    dailyUsageStats = dailyData
                )
            }
        }
    }

    private fun getGoals() {
        goalsJob?.cancel()
        goalsJob = vmScope.launch {
            // We get only 3 goals
            getGoals.getActiveGoals(factor = 1L, limitBy = 3).collectLatest { data ->
                goals.update { data }
            }
        }
    }

    private fun initializeGoals() {
        getGoals()
        vmScope.launch { modifyGoals.syncGoals() }
    }
}