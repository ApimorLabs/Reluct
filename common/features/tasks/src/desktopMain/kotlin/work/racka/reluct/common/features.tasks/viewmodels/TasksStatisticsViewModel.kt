package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.statistics.TasksStatistics

actual class TasksStatisticsViewModel(scope: CoroutineScope) {
    actual val host: TasksStatistics by inject(TasksStatistics::class.java) {
        parametersOf(scope)
    }
}