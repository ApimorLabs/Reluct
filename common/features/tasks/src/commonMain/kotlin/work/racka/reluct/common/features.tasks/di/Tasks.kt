package work.racka.reluct.common.features.tasks.di

import org.koin.core.KoinApplication
import org.koin.dsl.module
import work.racka.reluct.common.features.tasks.add_edit_task.repository.AddEditTaskRepository
import work.racka.reluct.common.features.tasks.add_edit_task.repository.AddEditTaskRepositoryImpl
import work.racka.reluct.common.features.tasks.completed_tasks.repository.CompletedTasksRepository
import work.racka.reluct.common.features.tasks.completed_tasks.repository.CompletedTasksRepositoryImpl
import work.racka.reluct.common.features.tasks.pending_tasks.repository.PendingTasksRepository
import work.racka.reluct.common.features.tasks.pending_tasks.repository.PendingTasksRepositoryImpl
import work.racka.reluct.common.features.tasks.search.repository.SearchTasksRepository
import work.racka.reluct.common.features.tasks.search.repository.SearchTasksRepositoryImpl
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepository
import work.racka.reluct.common.features.tasks.task_details.repository.TaskDetailsRepositoryImpl

object Tasks {

    fun KoinApplication.tasksModules() =
        this.apply {
            modules(
                commonModule(),
                Platform.platformTasksModule()
            )
        }

    private fun commonModule() = module {

        single<PendingTasksRepository> {
            PendingTasksRepositoryImpl(
                dao = get()
            )
        }

        single<CompletedTasksRepository> {
            CompletedTasksRepositoryImpl(
                dao = get()
            )
        }

        single<AddEditTaskRepository> {
            AddEditTaskRepositoryImpl(
                dao = get()
            )
        }

        single<SearchTasksRepository> {
            SearchTasksRepositoryImpl(
                dao = get()
            )
        }

        single<TaskDetailsRepository> {
            TaskDetailsRepositoryImpl(
                dao = get()
            )
        }
    }
}