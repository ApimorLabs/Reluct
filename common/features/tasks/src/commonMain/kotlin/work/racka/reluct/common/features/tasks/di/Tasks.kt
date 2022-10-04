package work.racka.reluct.common.features.tasks.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTaskViewModel
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksViewModel
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasksViewModel
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasksViewModel
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsViewModel
import work.racka.reluct.common.features.tasks.task_details.TaskDetailsViewModel

object Tasks {

    fun KoinApplication.tasksModules() {
        modules(Platform.platformTasksModule(), commonModule())
    }

    private fun commonModule() = module {
        commonViewModel { (taskId: String?) ->
            AddEditTaskViewModel(
                modifyTaskUseCase = get(),
                taskId = taskId
            )
        }

        commonViewModel {
            CompletedTasksViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get()
            )
        }

        commonViewModel {
            PendingTasksViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get()
            )
        }

        commonViewModel {
            SearchTasksViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get()
            )
        }

        commonViewModel {
            TasksStatisticsViewModel(
                modifyTasksUsesCase = get(),
                getGroupedTasksStats = get(),
                getWeekRangeFromOffset = get()
            )
        }

        commonViewModel { (taskId: String?) ->
            TaskDetailsViewModel(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                taskId = taskId
            )
        }
    }
}