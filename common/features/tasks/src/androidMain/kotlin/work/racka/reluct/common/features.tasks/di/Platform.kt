package work.racka.reluct.common.features.tasks.di

import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.viewmodels.AddEditViewModel
import work.racka.reluct.common.features.tasks.viewmodels.CompletedTasksViewModel
import work.racka.reluct.common.features.tasks.viewmodels.PendingTasksViewModel
import work.racka.reluct.common.features.tasks.viewmodels.TaskDetailsViewModel

internal actual object Platform {
    actual fun platformTasksModule() = module {
        viewModel {
            CompletedTasksViewModel(
                completedTasks = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        viewModel {
            PendingTasksViewModel(
                pendingTasks = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }

        viewModel {
            TaskDetailsViewModel(
                taskDetails = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }
        viewModel {
            AddEditViewModel(
                addEditTask = get(),
                backgroundDispatcher = Dispatchers.IO
            )
        }
    }
}
