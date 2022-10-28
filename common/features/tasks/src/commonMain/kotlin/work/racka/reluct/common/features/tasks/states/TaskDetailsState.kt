package work.racka.reluct.common.features.tasks.states

import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.domain.tasks.TaskLabel

data class TaskDetailsState(
    val taskState: TaskState = TaskState.Loading,
    val availableTaskLabels: List<TaskLabel> = emptyList()
)

sealed class TaskState {
    object Loading : TaskState()
    object NotFound : TaskState()
    object Deleted : TaskState()
    data class Data(val task: Task) : TaskState()
}
