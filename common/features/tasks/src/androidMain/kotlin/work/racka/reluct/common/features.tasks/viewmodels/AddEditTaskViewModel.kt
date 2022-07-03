package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTask

actual class AddEditTaskViewModel(taskId: String?) : ViewModel() {

    actual val host: AddEditTask by inject(AddEditTask::class.java) {
        parametersOf(taskId, viewModelScope)
    }
}