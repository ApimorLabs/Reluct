package work.racka.reluct.common.features.tasks.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.common.mvvm.koin.vm.commonViewModel
import work.racka.reluct.common.features.tasks.addEditTask.AddEditTaskViewModel
import work.racka.reluct.common.features.tasks.completedTasks.CompletedTasksViewModel
import work.racka.reluct.common.features.tasks.pendingTasks.PendingTasksViewModel
import work.racka.reluct.common.features.tasks.searchTasks.SearchTasksViewModel
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsViewModel
import work.racka.reluct.common.features.tasks.taskDetails.TaskDetailsViewModel

object Tasks {

    fun KoinApplication.tasksModules() {
        modules(commonModule())
    }

    private fun commonModule() = module {
        commonViewModel { (taskId: String?) ->
            AddEditTaskViewModel(
                modifyTaskUseCase = get(),
                manageTaskLabels = get(),
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
                manageTaskLabels = get(),
                taskId = taskId
            )
        }
    }
}
