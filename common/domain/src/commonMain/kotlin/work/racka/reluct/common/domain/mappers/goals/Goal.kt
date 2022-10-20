package work.racka.reluct.common.domain.mappers.goals

import work.racka.reluct.common.database.models.GoalDbObject
import work.racka.reluct.common.domain.usecases.app_info.GetAppInfo
import work.racka.reluct.common.model.domain.app_info.AppInfo
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalDuration
import work.racka.reluct.common.model.util.time.TimeUtils

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
    val formattedTimeRange = timeInterval?.let { range ->
        val start = TimeUtils.epochMillisToLocalDateTime(range.first).toString()
        val end = TimeUtils.epochMillisToLocalDateTime(range.last).toString()
        TimeUtils.getFormattedDateString(start)..TimeUtils.getFormattedDateString(end)
    }
    return Goal(
        id = id,
        name = name,
        description = description,
        isActive = isActive,
        relatedApps = listedApps,
        targetValue = targetValue,
        currentValue = currentValue,
        goalDuration = GoalDuration(
            goalInterval = goalInterval,
            timeRangeInMillis = timeInterval,
            formattedTimeRange = formattedTimeRange,
            selectedDaysOfWeek = daysOfWeekSelected
        ),
        goalType = goalType
    )
}