package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks

actual class PendingTasksViewModel(scope: CoroutineScope) {
    actual val host: PendingTasks by inject(PendingTasks::class.java) {
        parametersOf(scope)
    }
}