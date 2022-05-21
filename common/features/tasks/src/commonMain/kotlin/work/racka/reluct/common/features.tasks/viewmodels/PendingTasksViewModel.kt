package work.racka.reluct.common.features.tasks.viewmodels

import work.racka.reluct.common.features.tasks.pending_tasks.PendingTasks

expect class PendingTasksViewModel {
    val host: PendingTasks
}