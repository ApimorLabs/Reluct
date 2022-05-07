package work.racka.reluct.common.features.tasks.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.viewmodels.*

internal actual object Platform {
    actual fun platformTasksModule() = module {
        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            CompletedTasksViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                scope = viewModelScope
            )
        }

        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            PendingTasksViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                scope = viewModelScope
            )
        }

        factory { (taskId: String?) ->
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            TaskDetailsViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                taskId = taskId,
                scope = viewModelScope
            )
        }

        factory { (taskId: String?) ->
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            AddEditTaskViewModel(
                modifyTasksUseCase = get(),
                taskId = taskId,
                scope = viewModelScope
            )
        }

        factory {
            val viewModelScope = CoroutineScope(Dispatchers.Main.immediate)
            TasksStatisticsViewModel(
                modifyTasksUsesCase = get(),
                getWeeklyTasksUseCase = get(),
                getDailyTasksUseCase = get(),
                getWeekRangeFromOffset = get(),
                scope = viewModelScope
            )
        }
    }
}
