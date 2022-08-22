package work.racka.reluct.common.features.dashboard.overview

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.features.dashboard.overview.states.DashboardEvents
import work.racka.reluct.common.features.dashboard.overview.states.DashboardState
import work.racka.reluct.common.features.dashboard.overview.states.TodayScreenTimeState
import work.racka.reluct.common.features.dashboard.overview.states.TodayTasksState
import work.racka.reluct.common.model.domain.tasks.Task

class DashboardOverviewViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val modifyTasksUsesCase: ModifyTaskUseCase
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

    init {
        getPendingTasks()
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
}