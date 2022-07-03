package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks

actual class CompletedTasksViewModel : ViewModel() {

    actual val host: CompletedTasks by inject(CompletedTasks::class.java) {
        parametersOf(viewModelScope)
    }
}