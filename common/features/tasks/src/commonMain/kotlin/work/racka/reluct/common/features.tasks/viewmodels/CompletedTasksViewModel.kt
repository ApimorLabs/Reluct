package work.racka.reluct.common.features.tasks.viewmodels

import work.racka.reluct.common.features.tasks.completed_tasks.CompletedTasks

expect class CompletedTasksViewModel {
    val host: CompletedTasks
}