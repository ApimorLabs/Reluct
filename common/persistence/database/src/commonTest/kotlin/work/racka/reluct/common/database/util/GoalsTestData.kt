package work.racka.reluct.common.database.util

import work.racka.reluct.common.database.models.GoalDbObject
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType

object GoalsTestData {
    val goalsDbObjects = listOf(
        GoalDbObject(
            id = "goal_1",
            name = "Screen Time Goal",
            description = "Some Description",
            isActive = true,
            relatedApps = listOf(),
            targetValue = 180000,
            currentValue = 0,
            goalInterval = GoalInterval.Daily,
            timeInterval = null,
            daysOfWeekSelected = listOf(),
            goalType = GoalType.DeviceScreenTimeGoal
        ),
        GoalDbObject(
            id = "goal_2",
            name = "Social Media Limits",
            description = "Some Description",
            isActive = true,
            relatedApps = listOf("facebook", "twitter", "instagram"),
            targetValue = 180000,
            currentValue = 0,
            goalInterval = GoalInterval.Daily,
            timeInterval = null,
            daysOfWeekSelected = listOf(),
            goalType = GoalType.AppScreenTimeGoal
        ),
        GoalDbObject(
            id = "goal_3",
            name = "Do 10 Tasks a week",
            description = "Some Description",
            isActive = true,
            relatedApps = listOf(),
            targetValue = 10,
            currentValue = 0,
            goalInterval = GoalInterval.Weekly,
            timeInterval = null,
            daysOfWeekSelected = listOf(),
            goalType = GoalType.TasksGoal
        ),
        GoalDbObject(
            id = "goal_4",
            name = "Save $2000",
            description = "Some Description",
            isActive = true,
            relatedApps = listOf(),
            targetValue = 2000,
            currentValue = 1200,
            goalInterval = GoalInterval.Custom,
            timeInterval = 13434000L..1900393343L,
            daysOfWeekSelected = listOf(),
            goalType = GoalType.TasksGoal
        ),
    )
}