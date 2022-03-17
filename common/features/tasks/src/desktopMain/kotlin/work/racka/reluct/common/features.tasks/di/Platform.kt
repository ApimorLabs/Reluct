package work.racka.reluct.common.features.tasks.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.viewmodels.*

internal actual object Platform {
    actual fun platformTasksModule() = module {
        single {
            CompletedTasksViewModel(
                completedTasks = get(),
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        single {
            PendingTasksViewModel(
                pendingTasks = get(),
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        single {
            TaskDetailsViewModel(
                taskDetails = get(),
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        single {
            AddEditViewModel(
                addEditTask = get(),
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        single {
            SearchTasksViewModel(
                searchTasks = get(),
                scope = CoroutineScope(Dispatchers.Main)
            )
        }
    }
}
