package work.racka.reluct.common.database.dao.goals

import work.racka.reluct.common.database.models.GoalDbObject
import work.racka.reluct.common.database.tables.GoalsTable
import work.racka.reluct.common.database.tables.GoalsTableQueries
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.time.Week

object GoalsHelpers {

    fun GoalsTableQueries.insertGoalToDb(goal: GoalDbObject) {
        transaction { insertGoal(goal.asGoalsTable()) }
    }

    fun GoalsTableQueries.insertAllGoalsToDb(goals: List<GoalDbObject>) {
        transaction {
            goals.forEach { goal -> insertGoal(goal.asGoalsTable()) }
        }
    }

    val goalDbObjectMapper: (
        id: String,
        name: String,
        description: String,
        isActive: Boolean,
        relatedApps: List<String>,
        targetValue: Long,
        currentValue: Long,
        goalInterval: GoalInterval,
        timeInterval: LongRange?,
        daysOfWeekSelected: List<Week>,
        goalType: GoalType
    ) -> GoalDbObject = { id, name, description, isActive, relatedApps, targetValue, currentValue,
                          goalInterval, timeInterval, daysOfWeekSelected, goalType ->
        GoalDbObject(
            id = id,
            name = name,
            description = description,
            isActive = isActive,
            relatedApps = relatedApps,
            targetValue = targetValue,
            currentValue = currentValue,
            goalInterval = goalInterval,
            timeInterval = timeInterval,
            daysOfWeekSelected = daysOfWeekSelected,
            goalType = goalType
        )
    }

    private fun GoalDbObject.asGoalsTable() = GoalsTable(
        id = id,
        name = name,
        description = description,
        isActive = isActive,
        relatedApps = relatedApps,
        targetValue = targetValue,
        currentValue = currentValue,
        goalInterval = goalInterval,
        timeInterval = timeInterval,
        daysOfWeekSelected = daysOfWeekSelected,
        goalType = goalType
    )
}