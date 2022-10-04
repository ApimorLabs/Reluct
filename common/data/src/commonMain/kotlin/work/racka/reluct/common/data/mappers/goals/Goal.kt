package work.racka.reluct.common.data.mappers.goals

import work.racka.reluct.common.data.usecases.app_info.GetAppInfo
import work.racka.reluct.common.database.models.GoalDbObject
import work.racka.reluct.common.model.domain.app_info.AppInfo
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalDuration

fun Goal.asGoalDbObject() = GoalDbObject(
    id = id,
    name = name,
    description = description,
    isActive = isActive,
    relatedApps = relatedApps.map { it.packageName },
    targetValue = targetValue,
    currentValue = currentValue,
    goalInterval = goalDuration.goalInterval,
    timeInterval = goalDuration.timeRangeInMillis,
    daysOfWeekSelected = goalDuration.selectedDaysOfWeek,
    goalType = goalType
)

suspend fun GoalDbObject.asGoal(getAppInfo: GetAppInfo): Goal {
    val listedApps = relatedApps.map { packageName ->
        AppInfo(
            packageName = packageName,
            appName = getAppInfo.getAppName(packageName),
            appIcon = getAppInfo.getAppIcon(packageName)
        )
    }
    return Goal(
        id = id,
        name = name,
        description = description,
        isActive = isActive,
        relatedApps = listedApps,
        targetValue = targetValue,
        currentValue = currentValue,
        goalDuration = GoalDuration(goalInterval, timeInterval, daysOfWeekSelected),
        goalType = goalType
    )
}