package work.racka.reluct.common.features.goals.active.states

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalDuration
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.UUIDGen
import work.racka.reluct.common.model.util.time.Week

object DefaultGoals {
    fun predefined() = arrayOf(
        Goal(
            id = UUIDGen.getString(),
            name = "Reduce Daily Phone Usage",
            description = "Only use my phone for not more than 5 hours everyday",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = (1.8e7).toLong(),
            currentValue = 0,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Daily,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.values().toList().toImmutableList()
            ),
            goalType = GoalType.DeviceScreenTimeGoal
        ),
        Goal(
            id = UUIDGen.getString(),
            name = "Reduce App Usage",
            description = "Use certain app(s) for a limited time",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = (7.2e6).toLong(),
            currentValue = 0,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Daily,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.values().toList().toImmutableList()
            ),
            goalType = GoalType.AppScreenTimeGoal
        ),
        Goal(
            id = UUIDGen.getString(),
            name = "Complete Tasks",
            description = "Complete 50 Tasks Every Week",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = 10,
            currentValue = 0,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Weekly,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.values().toList().toImmutableList()
            ),
            goalType = GoalType.TasksGoal
        ),
        Goal(
            id = UUIDGen.getString(),
            name = "Save Money Weekly",
            description = "Save $50 every week",
            isActive = true,
            relatedApps = persistentListOf(),
            targetValue = 50,
            currentValue = 0,
            goalDuration = GoalDuration(
                goalInterval = GoalInterval.Weekly,
                timeRangeInMillis = null,
                formattedTimeRange = null,
                selectedDaysOfWeek = Week.values().toList().toImmutableList()
            ),
            goalType = GoalType.NumeralGoal
        )
    )

    fun emptyGoal() = Goal(
        id = UUIDGen.getString(),
        name = "",
        description = "",
        isActive = true,
        relatedApps = persistentListOf(),
        targetValue = 0,
        currentValue = 0,
        goalDuration = GoalDuration(
            goalInterval = GoalInterval.Daily,
            timeRangeInMillis = null,
            formattedTimeRange = null,
            selectedDaysOfWeek = Week.values().toList().toImmutableList()
        ),
        goalType = GoalType.NumeralGoal
    )
}
