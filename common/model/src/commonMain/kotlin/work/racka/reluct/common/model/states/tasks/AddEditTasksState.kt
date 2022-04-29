package work.racka.reluct.common.model.states.tasks

import work.racka.reluct.common.model.domain.tasks.EditTask

sealed class AddEditTasksState {
    data class Data(
        val task: EditTask? = null,
    ) : AddEditTasksState()

    object TaskSaved : AddEditTasksState()

    object Loading : AddEditTasksState()
}
