package work.racka.reluct.common.model.states.tasks

sealed class TasksSideEffect {
    object Nothing : TasksSideEffect()
    object AddTask : TasksSideEffect()
    data class ShowMessageDone(val isDone: Boolean) : TasksSideEffect()
    data class ShowMessage(val msg: String) : TasksSideEffect()
    data class DisplayErrorMsg(
        val msg: String,
    ) : TasksSideEffect()

    sealed class Navigation : TasksSideEffect() {
        data class NavigateToTaskDetails(
            val taskId: String,
        ) : Navigation()

        data class NavigateToEdit(
            val taskId: String,
        ) : Navigation()

        object GoBack : Navigation()
    }
}
