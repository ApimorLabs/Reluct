package work.racka.reluct.common.features.tasks.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.viewmodels.*

internal actual object Platform {
    actual fun platformTasksModule() = module {
        viewModel {
            CompletedTasksViewModel()
        }

        viewModel {
            PendingTasksViewModel()
        }

        viewModel { (taskId: String?) ->
            TaskDetailsViewModel(taskId = taskId)
        }

        viewModel { (taskId: String?) ->
            AddEditTaskViewModel(taskId = taskId)
        }

        viewModel {
            TasksStatisticsViewModel()
        }

        viewModel {
            SearchTasksViewModel()
        }
    }
}
