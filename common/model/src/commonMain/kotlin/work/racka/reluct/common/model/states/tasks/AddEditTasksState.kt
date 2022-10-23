package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.EditTask

sealed class AddEditTasksState {
    data class Data(
        val task: EditTask? = null,
    ) : AddEditTasksState()

    object TaskSaved : AddEditTasksState()

    object Loading : AddEditTasksState()
}

sealed class ModifyTaskState {
    data class Data(
        val task: EditTask,
        val isEdit: Boolean
    ) : ModifyTaskState()

    object Saved : ModifyTaskState()

    object Loading : ModifyTaskState()

    object NotFound : ModifyTaskState()
}
