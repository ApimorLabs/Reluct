package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTask
import work.racka.reluct.common.features.tasks.add_edit_task.container.AddEditTaskContainerHost
import work.racka.reluct.common.features.tasks.add_edit_task.container.AddEditTaskContainerHostImpl

actual class AddEditViewModel(
    addEditTask: AddEditTask,
    backgroundDispatcher: CoroutineDispatcher,
    scope: CoroutineScope
) {
    val host: AddEditTaskContainerHost = AddEditTaskContainerHostImpl(
        addEditTask = addEditTask,
        backgroundDispatcher = backgroundDispatcher,
        scope = scope
    )
}