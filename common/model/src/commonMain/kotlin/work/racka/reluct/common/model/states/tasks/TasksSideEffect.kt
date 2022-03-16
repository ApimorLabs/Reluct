package work.racka.reluct.common.model.states.tasks

sealed class TasksSideEffect {
    object Nothing : TasksSideEffect()
    object AddTask : TasksSideEffect()
    data class TaskDone(val isDone: Boolean) : TasksSideEffect()
    data class ShowSnackbar(val msg: String) : TasksSideEffect()
    data class DisplayErrorMsg(
        val msg: String
    ) : TasksSideEffect()

    sealed class Navigation : TasksSideEffect() {
        data class NavigateToTaskDetails(
            val taskId: Long
        ) : Navigation()

        data class NavigateToEdit(
            val taskId: Long
        ) : Navigation()

        object GoBack : Navigation()
    }
}
