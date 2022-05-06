package work.racka.reluct.common.features.tasks.statistics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetDailyTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.DailyTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState
import work.racka.reluct.common.model.states.tasks.WeeklyTasksState

internal class TasksStatisticsImpl(
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val getDailyTasksUseCase: GetDailyTasksUseCase,
    private val scope: CoroutineScope,
) : TasksStatistics {

    private val weekOffset: MutableStateFlow<Int> = MutableStateFlow(0)
    private val selectedDay: MutableStateFlow<Int> = MutableStateFlow(0)
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
            val dayCompletedTasks =
                getDailyTasksUseCase.getDailyCompletedTasks(weekOffset = weekOffset.value,
                    dayIsoNumber = selectedDay.value)
            val dayPendingTasks =
                getDailyTasksUseCase.getDailyPendingTasks(weekOffset = weekOffset.value,
                    dayIsoNumber = selectedDay.value)

            combine(dayCompletedTasks, dayPendingTasks) { completed, pending ->
                if (completed.isEmpty() && pending.isEmpty()) {
                    dailyTasksState.update {
                        DailyTasksState.Data(dayCompletedTasks = completed,
                            dayPendingTasks = pending)
                    }
                } else dailyTasksState.update { DailyTasksState.Empty }
                Pair(completed.size, pending.size)
            }.collectLatest { tasksPair ->
                if (tasksPair.first.plus(tasksPair.second) != 0) {
                    weeklyTasksState.update {
                        WeeklyTasksState.Data(tasksCompleted = tasksPair.first,
                            tasksPending = tasksPair.second)
                    }
                } else weeklyTasksState.update { WeeklyTasksState.Empty }
            }
        }
    }

    override fun selectDay(selectedDayIsoNumber: Int) {
        dailyTasksState.update { DailyTasksState.Loading }
        selectedDay.update { selectedDayIsoNumber }
    }

    override fun updateWeekOffset(weekOffset: Int) {
        weeklyTasksState.update { WeeklyTasksState.Loading }
        this.weekOffset.update { weekOffset }
    }

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksEvents.ShowMessageDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}