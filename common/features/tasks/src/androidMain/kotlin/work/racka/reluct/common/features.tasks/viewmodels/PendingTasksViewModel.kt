package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks

actual class PendingTasksViewModel : ViewModel() {

    actual val host: PendingTasks by inject(PendingTasks::class.java) {
        parametersOf(viewModelScope)
    }
}