package work.racka.reluct.common.features.tasks.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.viewmodels.*

internal actual object Platform {
    actual fun platformTasksModule() = module {
        factory {
            CompletedTasksViewModel(
                completedTasks = get(),
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        factory {
            PendingTasksViewModel(
                pendingTasks = get(),
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        factory { (taskId: Long?) ->
            TaskDetailsViewModel(
                taskDetails = get(),
                taskId = taskId,
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        factory { (taskId: Long?) ->
            AddEditTaskViewModel(
                addEditTask = get(),
                taskId = taskId,
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        factory {
            SearchTasksViewModel(
                searchTasks = get(),
                scope = CoroutineScope(Dispatchers.Main)
            )
        }
    }
}
