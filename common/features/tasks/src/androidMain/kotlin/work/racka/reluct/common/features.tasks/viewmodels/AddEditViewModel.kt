package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.features.tasks.add_edit_task.container.AddEditTaskContainerHost
import work.racka.reluct.common.features.tasks.add_edit_task.container.AddEditTaskContainerHostImpl
import work.racka.reluct.common.features.tasks.add_edit_task.repository.AddEditTaskRepository

actual class AddEditViewModel(
    addEditTask: AddEditTaskRepository
) : ViewModel() {
    val host: AddEditTaskContainerHost by lazy {
        AddEditTaskContainerHostImpl(
            addEditTask = addEditTask,
            scope = viewModelScope
        )
    }
}