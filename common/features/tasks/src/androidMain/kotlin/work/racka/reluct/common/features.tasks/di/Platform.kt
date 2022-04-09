package work.racka.reluct.common.features.tasks.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.viewmodels.*

internal actual object Platform {
    actual fun platformTasksModule() = module {
        viewModel {
            CompletedTasksViewModel(
                completedTasks = get()
            )
        }

        viewModel {
            PendingTasksViewModel(
                pendingTasks = get()
            )
        }

        viewModel { (taskId: Long?) ->
            TaskDetailsViewModel(
                taskDetails = get(),
                taskId = taskId
            )
        }

        viewModel { (taskId: Long?) ->
            AddEditTaskViewModel(
                addEditTask = get(),
                taskId = taskId
            )
        }

        viewModel {
            SearchTasksViewModel(
                searchTasks = get()
            )
        }
    }
}
