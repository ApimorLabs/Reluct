package work.racka.reluct.common.model.domain.tasks

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val done: Boolean,
    val overdue: Boolean,
    val dueDate: String,
    val dueTime: String,
    val timeLeftLabel: String,
    val reminder: String,
    val completedDateAndTime: String,
)