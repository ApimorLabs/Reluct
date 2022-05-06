package work.racka.reluct.common.features.tasks.statistics

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksEvents
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState

internal class TasksStatisticsImpl(
    private val modifyTasksUsesCase: ModifyTaskUseCase,
    private val scope: CoroutineScope,
) : TasksStatistics {

    private val _uiState: MutableStateFlow<TasksStatisticsState> =
        MutableStateFlow(TasksStatisticsState())
    private val _events: Channel<TasksEvents> = Channel()

    override val uiState: StateFlow<TasksStatisticsState>
        get() = _uiState

    override val events: Flow<TasksEvents>
        get() = _events.receiveAsFlow()

    override fun selectDay(selectedDayIsoNumber: Int) {
        TODO("Not yet implemented")
    }

    override fun updateWeekOffset(weekOffset: Int) {
        TODO("Not yet implemented")
    }

    override fun toggleDone(task: Task, isDone: Boolean) {
        modifyTasksUsesCase.toggleTaskDone(task, isDone)
        _events.trySend(TasksEvents.ShowMessageDone(isDone))
    }

    override fun navigateToTaskDetails(taskId: String) {
        _events.trySend(TasksEvents.Navigation.NavigateToTaskDetails(taskId))
    }
}