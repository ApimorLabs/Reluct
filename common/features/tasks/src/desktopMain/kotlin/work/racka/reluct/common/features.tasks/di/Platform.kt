package work.racka.reluct.common.features.tasks.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.viewmodels.*

internal actual object Platform {
    actual fun platformTasksModule() = module {
        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            CompletedTasksViewModel(scope = viewModelScope)
        }

        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            PendingTasksViewModel(scope = viewModelScope)
        }

        factory { (taskId: String?) ->
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            TaskDetailsViewModel(
                taskId = taskId,
                scope = viewModelScope
            )
        }

        factory { (taskId: String?) ->
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            AddEditTaskViewModel(
                taskId = taskId,
                scope = viewModelScope
            )
        }

        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            TasksStatisticsViewModel(scope = viewModelScope)
        }

        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            SearchTasksViewModel(scope = viewModelScope)
        }
    }
}
