package work.racka.reluct.android.compose.components.util

import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalDuration
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.UUIDGen
import work.racka.reluct.common.model.util.time.Week

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
        reminderFormatted = "",
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
        reminderFormatted = "",
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
        reminderFormatted = "",
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
        reminderFormatted = "",
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
        reminderFormatted = "",
        completedDateAndTime = "Time Here"
    )

    val goals = arrayOf(
        Goal(
            id = "1",
            name = "Complete Tasks",
            description = "Complete 50 Tasks Every Week",
            isActive = true,
            relatedApps = emptyList(),
            targetValue = 10,
            currentValue = 3,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Weekly,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.values().toList()
            ),
            goalType = GoalType.TasksGoal
        ),
        Goal(
            id = "2",
            name = "Save Money Weekly",
            description = "Save $250 every week",
            isActive = true,
            relatedApps = emptyList(),
            targetValue = 250,
            currentValue = 150,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Weekly,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.values().toList()
            ),
            goalType = GoalType.NumeralGoal
        ),
        Goal(
            id = UUIDGen.getString(),
            name = "Reduce Daily Phone Usage",
            description = "Only use my phone for not more than 5 hours everyday",
            isActive = true,
            relatedApps = emptyList(),
            targetValue = (1.8e7).toLong(),
            currentValue = (9.2e7).toLong(),
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Daily,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = listOf()
            ),
            goalType = GoalType.DeviceScreenTimeGoal
        )
    )
}