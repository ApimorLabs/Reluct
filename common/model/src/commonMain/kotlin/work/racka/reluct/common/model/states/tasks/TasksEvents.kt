package work.racka.reluct.common.model.states.tasks

sealed class TasksEvents {
    object Nothing : TasksEvents()
    data class ShowMessageDone(val isDone: Boolean) : TasksEvents()
    data class ShowMessage(val msg: String) : TasksEvents()
    data class DisplayErrorMsg(
        val msg: String,
    ) : TasksEvents()

    sealed class Navigation : TasksEvents() {
        data class NavigateToTaskDetails(
            val taskId: String,
        ) : Navigation()

        data class NavigateToEdit(
            val taskId: String,
        ) : Navigation()

        object GoBack : Navigation()
    }
}
