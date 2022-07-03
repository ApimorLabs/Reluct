package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks

actual class CompletedTasksViewModel internal constructor(scope: CoroutineScope) {

    actual val host: CompletedTasks by inject(CompletedTasks::class.java) {
        parametersOf(scope)
    }
}