package work.racka.reluct.common.features.dashboard.overview

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.app_usage.GetDailyUsageStats
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.dashboard.overview.states.DashboardEvents
import work.racka.reluct.common.features.dashboard.overview.states.DashboardState
import work.racka.reluct.common.features.dashboard.overview.states.TodayScreenTimeState
import work.racka.reluct.common.features.dashboard.overview.states.TodayTasksState
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.time.WeekUtils

class DashboardOverviewViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val getDailyUsageStats: GetDailyUsageStats,
) : CommonViewModel() {

    private val todayScreenTimeState: MutableStateFlow<TodayScreenTimeState> =
        MutableStateFlow(TodayScreenTimeState.Nothing)
    private val todayTasksState: MutableStateFlow<TodayTasksState> =
        MutableStateFlow(TodayTasksState.Nothing)

    val uiState: StateFlow<DashboardState> = combine(
        todayScreenTimeState, todayTasksState
    ) { todayScreenTimeState, todayTasksState ->
        DashboardState(
            todayScreenTimeState = todayScreenTimeState,
            todayTasksState = todayTasksState
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardState()
    )

    private val _events = Channel<DashboardEvents>(Channel.UNLIMITED)
    val events: Flow<DashboardEvents> = _events.receiveAsFlow()

    private var pendingTasksJob: Job? = null
    private var dailyScreenTimeJob: Job? = null
    private var permissionsJob: Job? = null

    private val permissionGranted = MutableStateFlow(false)

    init {
        initializeScreenTimeData()
        getPendingTasks()
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
            val dailyData = getDailyUsageStats.invoke(dayIsoNumber = today.isoDayNumber)
            todayScreenTimeState.update {
                TodayScreenTimeState.Data(
                    dailyUsageStats = dailyData
                )
            }
        }
    }
}