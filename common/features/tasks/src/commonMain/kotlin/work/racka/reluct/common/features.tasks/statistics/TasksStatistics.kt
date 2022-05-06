package work.racka.reluct.common.features.tasks.statistics

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState

interface TasksStatistics {
    val uiState: StateFlow<TasksStatisticsState>
    val events: Flow<TasksEvents>
    fun selectDay(selectedDayIsoNumber: Int)
    fun updateWeekOffset(weekOffsetValue: Int)
    fun toggleDone(task: Task, isDone: Boolean)
    fun navigateToTaskDetails(taskId: String)
}