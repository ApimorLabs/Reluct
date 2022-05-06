package work.racka.reluct.common.features.tasks.statistics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetDailyTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetWeeklyTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.DailyTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState
import work.racka.reluct.common.model.states.tasks.WeeklyTasksState
import work.racka.reluct.common.model.util.time.WeekUtils

internal class TasksStatisticsImpl(
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val getWeeklyTasksUseCase: GetWeeklyTasksUseCase,
    private val getDailyTasksUseCase: GetDailyTasksUseCase,
    private val scope: CoroutineScope,
) : TasksStatistics {

    private val weekOffset: MutableStateFlow<Int> = MutableStateFlow(0)
    private val selectedDay: MutableStateFlow<Int> =
        MutableStateFlow(WeekUtils.currentDayOfWeek().isoDayNumber)
    private val weeklyTasksState: MutableStateFlow<WeeklyTasksState> =
        MutableStateFlow(WeeklyTasksState.Loading)
    private val dailyTasksState: MutableStateFlow<DailyTasksState> =
        MutableStateFlow(DailyTasksState.Loading)


    override val uiState: StateFlow<TasksStatisticsState> = combine(
        weekOffset, selectedDay, weeklyTasksState, dailyTasksState
    ) { weekOffset, selectedDay, weeklyTasksState, dailyTasksState ->
        TasksStatisticsState(
            weekOffset = weekOffset,
            selectedDay = selectedDay,
            weeklyTasksState = weeklyTasksState,
            dailyTasksState = dailyTasksState
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksStatisticsState()
    )

    private val _events: Channel<TasksEvents> = Channel()
    override val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    init {
        getData()
    }

    private fun getData() {
        scope.launch {
            getDailyTasksUseCase(weekOffset = weekOffset.value,
                dayIsoNumber = selectedDay.value).collectLatest { tasks ->
                if (tasks.completedTasks.isNotEmpty() && tasks.pendingTasks.isNotEmpty()) {
                    dailyTasksState.update { DailyTasksState.Data(tasks = tasks) }
                } else dailyTasksState.update { DailyTasksState.Empty }
            }
        }

        scope.launch {
            getWeeklyTasksUseCase(weekOffset = weekOffset.value).collectLatest { weeklyTasks ->
                if (weeklyTasks.isNotEmpty()) {
                    weeklyTasksState.update { WeeklyTasksState.Data(weeklyTasks = weeklyTasks) }
                } else weeklyTasksState.update { WeeklyTasksState.Empty }
            }
        }
    }

    override fun selectDay(selectedDayIsoNumber: Int) {
        dailyTasksState.update { DailyTasksState.Loading }
        selectedDay.update { selectedDayIsoNumber }
    }

    override fun updateWeekOffset(weekOffsetValue: Int) {
        weeklyTasksState.update { WeeklyTasksState.Loading }
        weekOffset.update { weekOffsetValue }
    }

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksEvents.ShowMessageDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}