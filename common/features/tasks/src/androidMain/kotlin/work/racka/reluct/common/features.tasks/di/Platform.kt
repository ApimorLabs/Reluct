package work.racka.reluct.common.features.tasks.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.viewmodels.*

internal actual object Platform {
    actual fun platformTasksModule() = module {
        viewModel {
            CompletedTasksViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get()
            )
        }

        viewModel {
            PendingTasksViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get()
            )
        }

        viewModel { (taskId: String?) ->
            TaskDetailsViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                taskId = taskId
            )
        }

        viewModel { (taskId: String?) ->
            AddEditTaskViewModel(
                modifyTasksUseCase = get(),
                taskId = taskId
            )
        }

        viewModel {
            TasksStatisticsViewModel(
                modifyTasksUsesCase = get(),
                getWeeklyTasksUseCase = get(),
                getDailyTasksUseCase = get(),
                getWeekRangeFromOffset = get()
            )
        }
    }
}
