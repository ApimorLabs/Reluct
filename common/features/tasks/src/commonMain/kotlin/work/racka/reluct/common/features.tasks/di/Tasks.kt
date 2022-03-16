package work.racka.reluct.common.features.tasks.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTask
import work.racka.reluct.common.features.tasks.add_edit_task.AddEditTaskImpl
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks
import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasksImpl
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks
import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasksImpl
import work.racka.reluct.common.features.tasks.search.SearchTasks
import work.racka.reluct.common.features.tasks.search.SearchTasksImpl
import work.racka.reluct.common.features.tasks.task_details.TaskDetails
import work.racka.reluct.common.features.tasks.task_details.TaskDetailsImpl

object Tasks {

    fun KoinApplication.tasksModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformTasksModule()
            )
        }

    private fun commonModule() = module {

        single<PendingTasks> {
            PendingTasksImpl(
                dao = get()
            )
        }

        single<CompletedTasks> {
            CompletedTasksImpl(
                dao = get()
            )
        }

        single<AddEditTask> {
            AddEditTaskImpl(
                dao = get()
            )
        }

        single<SearchTasks> {
            SearchTasksImpl(
                dao = get()
            )
        }

        single<TaskDetails> {
            TaskDetailsImpl(
                dao = get()
            )
        }
    }
}