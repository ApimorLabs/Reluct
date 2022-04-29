package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

sealed class TaskDetailsState {
    data class Data(
        val task: Task? = null,
    ) : TaskDetailsState()

    object TaskDeleted : TaskDetailsState()

    object Loading : TaskDetailsState()
}
