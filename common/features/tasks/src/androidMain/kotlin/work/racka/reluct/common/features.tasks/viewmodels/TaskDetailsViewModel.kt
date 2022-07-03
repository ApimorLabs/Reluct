package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.task_details.TaskDetails

actual class TaskDetailsViewModel(taskId: String?) : ViewModel() {
    actual val host: TaskDetails by inject(TaskDetails::class.java) {
        parametersOf(taskId, viewModelScope)
    }
}