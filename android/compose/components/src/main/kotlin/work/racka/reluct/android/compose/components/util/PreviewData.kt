package work.racka.reluct.android.compose.components.util

import work.racka.reluct.common.model.domain.tasks.Task

object PreviewData {
    val task1 = Task(
        id = "1L",
        title = "Task 1 Title Goes Here",
        description = "This is a long description. This is a long description. This is a long description. This is a long description.",
        done = false,
        overdue = false,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "In 3 hours",
        reminder = "",
        completedDateAndTime = "Time Here"
    )

    val task2 = Task(
        id = "2L",
        title = "Task 2 Title Goes Here",
        description = "This is a long description. This is a long description. This is a long description. This is a long description.",
        done = true,
        overdue = false,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "In 3 hours",
        reminder = "",
        completedDateAndTime = "Time Here"
    )

    val task3 = Task(
        id = "3L",
        title = "Task 3 Title Goes Here",
        description = "This is a long description. This is a long description. This is a long description. This is a long description.",
        done = true,
        overdue = true,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "In 3 hours",
        reminder = "",
        completedDateAndTime = "Time Here"
    )

    val task4 = Task(
        id = "4L",
        title = "Task 4 Title Goes Here",
        description = "This is a long description. This is a long description. This is a long description. This is a long description.",
        done = false,
        overdue = true,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "3 hours ago",
        reminder = "",
        completedDateAndTime = "Time Here"
    )

    val task5 = Task(
        id = "2L",
        title = "Task 2 Title Goes Here",
        description = "This is a long description. This is a long description. This is a long description. This is a long description.",
        done = true,
        overdue = true,
        dueDate = "Thu, 2 Feb",
        dueTime = "15:30",
        timeLeftLabel = "In 3 hours",
        reminder = "",
        completedDateAndTime = "Time Here"
    )
}