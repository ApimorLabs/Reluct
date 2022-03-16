package work.racka.reluct.common.model.states.tasks

sealed class TasksSideEffect {
    object Nothing : TasksSideEffect()
    object AddTask : TasksSideEffect()
    data class TaskDone(val isDone: Boolean) : TasksSideEffect()
    data class NavigateToTaskDetails(
        val taskId: Long
    ) : TasksSideEffect()
}
