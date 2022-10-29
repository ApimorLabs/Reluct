package work.racka.reluct.common.features.dashboard.statistics

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.features.screen_time.statistics.ScreenTimeStatsViewModel
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsEvents
import work.racka.reluct.common.features.screen_time.statistics.states.all_stats.ScreenTimeStatsState
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsViewModel
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState

class DashboardStatisticsViewModel(
    private val screenTimeStatsViewModel: ScreenTimeStatsViewModel,
    private val tasksStatsViewModel: TasksStatisticsViewModel
) : CommonViewModel() {

    val screenTimeUiState: StateFlow<ScreenTimeStatsState> = screenTimeStatsViewModel.uiState
    val tasksStatsUiState: StateFlow<TasksStatisticsState> = tasksStatsViewModel.uiState

    val tasksStatsEvents: Flow<TasksEvents> = tasksStatsViewModel.events
    val screenTimeEvents: Flow<ScreenTimeStatsEvents> = screenTimeStatsViewModel.events

    init {
        screenTimeStatsViewModel.permissionCheck(true)
    }

    // For Tasks
    fun tasksSelectDay(dayIsoNumber: Int) = tasksStatsViewModel.selectDay(dayIsoNumber)
    fun toggleTaskDone(task: Task, isDone: Boolean) = tasksStatsViewModel.toggleDone(task, isDone)

    // For Screen Time
    fun screenTimeSelectDay(dayIsoNumber: Int) = screenTimeStatsViewModel.selectDay(dayIsoNumber)

    fun selectAppTimeLimit(packageName: String) =
        screenTimeStatsViewModel.selectAppTimeLimit(packageName)

    fun saveAppTimeLimit(hours: Int, minutes: Int) =
        screenTimeStatsViewModel.saveTimeLimit(hours, minutes)

    override fun destroy() {
        screenTimeStatsViewModel.destroy()
        tasksStatsViewModel.destroy()
    }
}