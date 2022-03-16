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
                backgroundDispatcher = Dispatchers.IO,
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        single {
            PendingTasksViewModel(
                pendingTasks = get(),
                backgroundDispatcher = Dispatchers.IO,
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        single {
            TaskDetailsViewModel(
                taskDetails = get(),
                backgroundDispatcher = Dispatchers.IO,
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        single {
            AddEditViewModel(
                addEditTask = get(),
                backgroundDispatcher = Dispatchers.IO,
                scope = CoroutineScope(Dispatchers.Main)
            )
        }

        single {
            SearchTasksViewModel(
                searchTasks = get(),
                backgroundDispatcher = Dispatchers.IO,
                scope = CoroutineScope(Dispatchers.Main)
            )
        }
    }
}
