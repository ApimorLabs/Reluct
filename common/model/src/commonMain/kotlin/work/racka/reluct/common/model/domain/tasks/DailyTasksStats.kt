package work.racka.reluct.common.model.domain.tasks

data class DailyTasksStats(
    val completedTasks: List<Task> = emptyList(),
    val pendingTasks: List<Task> = emptyList(),
) {
    val completedTasksCount
        get() = completedTasks.size
    val pendingTasksCount
        get() = pendingTasks.size
}
