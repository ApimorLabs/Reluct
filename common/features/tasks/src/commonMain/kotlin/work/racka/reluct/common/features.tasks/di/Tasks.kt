package work.racka.reluct.common.features.tasks.di

import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTask
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTaskImpl
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksImpl
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasksImpl
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasks
import work.racka.reluct.common.features.tasks.search_tasks.SearchTasksImpl
import work.racka.reluct.common.features.tasks.statistics.TasksStatistics
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsImpl
import work.racka.reluct.common.features.tasks.task_details.TaskDetails
import work.racka.reluct.common.features.tasks.task_details.TaskDetailsImpl

object Tasks {

    fun KoinApplication.tasksModules() {
        modules(Platform.platformTasksModule(), commonModule())
    }

    private fun commonModule() = module {
        factory<AddEditTask> { (taskId: String?, scope: CoroutineScope) ->
            AddEditTaskImpl(
                modifyTaskUseCase = get(),
                taskId = taskId,
                scope = scope
            )
        }

        factory<CompletedTasks> { (scope: CoroutineScope) ->
            CompletedTasksImpl(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                scope = scope
            )
        }

        factory<PendingTasks> { (scope: CoroutineScope) ->
            PendingTasksImpl(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                scope = scope
            )
        }

        factory<SearchTasks> { (scope: CoroutineScope) ->
            SearchTasksImpl(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                scope = scope
            )
        }

        factory<TasksStatistics> { (scope: CoroutineScope) ->
            TasksStatisticsImpl(
                modifyTasksUsesCase = get(),
                getWeeklyTasksUseCase = get(),
                getDailyTasksUseCase = get(),
                getWeekRangeFromOffset = get(),
                scope = scope
            )
        }

        factory<TaskDetails> { (taskId: String?, scope: CoroutineScope) ->
            TaskDetailsImpl(
                getTasksUseCase = get(),
                modifyTasksUsesCase = get(),
                taskId = taskId,
                scope = scope
            )
        }
    }
}