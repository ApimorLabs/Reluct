package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTask
import work.racka.reluct.common.features.tasks.add_edit_task.container.AddEditTaskContainerHost
import work.racka.reluct.common.features.tasks.add_edit_task.container.AddEditTaskContainerHostImpl

actual class AddEditViewModel(
    addEditTask: AddEditTask,
    backgroundDispatcher: CoroutineDispatcher
) : ViewModel() {
    val host: AddEditTaskContainerHost by lazy {
        AddEditTaskContainerHostImpl(
            addEditTask = addEditTask,
            backgroundDispatcher = backgroundDispatcher,
            scope = viewModelScope
        )
    }
}