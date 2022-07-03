package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import work.racka.reluct.common.features.tasks.statistics.TasksStatistics

actual class TasksStatisticsViewModel : ViewModel() {
    actual val host: TasksStatistics by inject(TasksStatistics::class.java) {
        parametersOf(viewModelScope)
    }
}