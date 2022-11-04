package work.racka.reluct.common.model.states.tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.TaskLabel

sealed class AddEditTasksState {
    data class Data(
        val task: EditTask? = null,
    ) : AddEditTasksState()

    object TaskSaved : AddEditTasksState()

    object Loading : AddEditTasksState()
}

data class AddEditTaskState(
    val modifyTaskState: ModifyTaskState = ModifyTaskState.Loading,
    val availableTaskLabels: ImmutableList<TaskLabel> = persistentListOf()
)

sealed class ModifyTaskState {
    data class Data(
        val task: EditTask,
        val isEdit: Boolean
    ) : ModifyTaskState()

    object Saved : ModifyTaskState()

    object Loading : ModifyTaskState()

    object NotFound : ModifyTaskState()
}
