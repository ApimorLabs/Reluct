package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.Task

sealed class TasksState {
    data class IncompleteTasks(
        val tasks: List<Task> = listOf()
    ) : TasksState()

    data class DoneTasks(
        val tasks: List<Task> = listOf()
    ) : TasksState()

    object Loading : TasksState()

    companion object {
        val EmptyIncompleteTasks = IncompleteTasks()
        val EmptyDoneTasks = DoneTasks()
    }
}
