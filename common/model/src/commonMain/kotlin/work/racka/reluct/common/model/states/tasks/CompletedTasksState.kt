package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

sealed class CompletedTasksState {
    data class Data(
        val tasks: Map<String, List<Task>> = mapOf(),
    ) : CompletedTasksState()

    object Loading : CompletedTasksState()
}
