package work.racka.reluct.common.features.tasks.statistics

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.data.usecases.tasks.GetGroupedTasksStats
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.data.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.DailyTasksState
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState
import work.racka.reluct.common.model.states.tasks.WeeklyTasksState
import work.racka.reluct.common.model.util.time.WeekUtils

class TasksStatisticsViewModel(
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val getGroupedTasksStats: GetGroupedTasksStats,
    private val getWeekRangeFromOffset: GetWeekRangeFromOffset,
) : CommonViewModel() {

    private val weekOffset: MutableStateFlow<Int> = MutableStateFlow(0)
    private val selectedWeekText: MutableStateFlow<String> = MutableStateFlow("...")
    private val selectedDay: MutableStateFlow<Int> =
        MutableStateFlow(WeekUtils.currentDayOfWeek().isoDayNumber)
    private val weeklyTasksState: MutableStateFlow<WeeklyTasksState> =
        MutableStateFlow(WeeklyTasksState.Loading())
    private val dailyTasksState: MutableStateFlow<DailyTasksState> =
        MutableStateFlow(DailyTasksState.Loading())

    private lateinit var collectDailyTasksJob: Job
    private lateinit var collectWeeklyTasksJob: Job


    val uiState: StateFlow<TasksStatisticsState> = combine(
        weekOffset, selectedWeekText, selectedDay, weeklyTasksState, dailyTasksState
    ) { weekOffset, selectedWeekText, selectedDay, weeklyTasksState, dailyTasksState ->
        TasksStatisticsState(
            weekOffset = weekOffset,
            selectedWeekText = selectedWeekText,
            selectedDay = selectedDay,
            weeklyTasksState = weeklyTasksState,
            dailyTasksState = dailyTasksState
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TasksStatisticsState()
    )

    private val _events: Channel<TasksEvents> = Channel()
    val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    init {
        getData()
    }

    private fun getData() {
        getWeeklyData()
        getDailyData()
    }

    private fun getDailyData() {
        collectDailyTasksJob = vmScope.launch {
            getGroupedTasksStats.dailyTasks(
                weekOffset = weekOffset.value,
                dayIsoNumber = selectedDay.value
            ).collectLatest { tasks ->
                if (tasks.completedTasks.isNotEmpty() || tasks.pendingTasks.isNotEmpty()) {
                    dailyTasksState.update {
                        DailyTasksState.Data(tasks = tasks, dayTextValue = tasks.dateFormatted)
                    }
                } else dailyTasksState.update {
                    DailyTasksState.Empty(dayTextValue = tasks.dateFormatted)
                }
            }
        }
    }

    private fun getWeeklyData() {
        collectWeeklyTasksJob = vmScope.launch {
            val weekOffsetText = getWeekRangeFromOffset.invoke(weekOffset.value)
            selectedWeekText.update { weekOffsetText }
            getGroupedTasksStats.weeklyTasks(weekOffset = weekOffset.value)
                .collectLatest { weeklyTasks ->
                    if (weeklyTasks.isNotEmpty()) {
                        var totalTasksCount = 0
                        weeklyTasks.entries.forEach {
                            totalTasksCount += (it.value.completedTasksCount + it.value.pendingTasksCount)
                        }
                        weeklyTasksState.update {
                            WeeklyTasksState.Data(
                                tasks = weeklyTasks,
                                totalTaskCount = totalTasksCount
                            )
                        }
                    } else weeklyTasksState.update { WeeklyTasksState.Empty }
                }
        }
    }

    fun selectDay(selectedDayIsoNumber: Int) {
        dailyTasksState.update { DailyTasksState.Loading(dayTextValue = it.dayText) }
        selectedDay.update { selectedDayIsoNumber }
        collectDailyTasksJob.cancel()
        getDailyData()
    }

    fun updateWeekOffset(weekOffsetValue: Int) {
        weeklyTasksState.update { WeeklyTasksState.Loading(totalTaskCount = it.totalWeekTasksCount) }
        weekOffset.update { weekOffsetValue }
        collectDailyTasksJob.cancel()
        collectWeeklyTasksJob.cancel()
        getData()
    }

    fun toggleDone(task: Task, isDone: Boolean) {
        vmScope.launch {
            modifyTasksUsesCase.toggleTaskDone(task, isDone)
            _events.send(TasksEvents.ShowMessageDone(isDone, task.title))
        }
    }

    fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}