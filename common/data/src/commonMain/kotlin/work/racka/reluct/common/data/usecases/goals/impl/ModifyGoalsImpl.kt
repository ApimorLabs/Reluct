package work.racka.reluct.common.data.usecases.goals.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager
import work.racka.reluct.common.data.mappers.goals.asGoalDbObject
import work.racka.reluct.common.data.usecases.goals.ModifyGoals
import work.racka.reluct.common.data.usecases.tasks.GetGroupedTasksStats
import work.racka.reluct.common.database.dao.goals.GoalsDao
import work.racka.reluct.common.database.models.GoalDbObject
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.WeekUtils
import work.racka.reluct.common.system_service.haptics.HapticFeedback

internal class ModifyGoalsImpl(
    private val goalsDao: GoalsDao,
    private val haptics: HapticFeedback,
    private val usageDataManager: UsageDataManager,
    private val getGroupedTasksStats: GetGroupedTasksStats,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ModifyGoals {

    override suspend fun saveGoal(goal: Goal) = withContext(dispatcher) {
        goalsDao.insertGoal(goal.asGoalDbObject())
        haptics.click()
    }

    override suspend fun deleteGoal(id: String) = withContext(dispatcher) {
        goalsDao.deleteGoal(id)
        haptics.heavyClick()
    }

    override suspend fun toggleGoalActiveState(isActive: Boolean, id: String) =
        withContext(dispatcher) {
            goalsDao.toggleGoalActiveState(isActive = isActive, id = id)
            haptics.tick()
        }

    override suspend fun updateGoalCurrentValue(id: String, currentValue: Long) =
        withContext(dispatcher) {
            goalsDao.updateGoalCurrentValue(id = id, currentValue = currentValue)
            haptics.click()
        }

    override suspend fun syncGoals() {
        val goals = goalsDao.getActiveGoals(factor = 0).first().map { goal ->
            when (goal.goalType) {
                GoalType.TasksGoal -> manageTasksGoalType(goal)
                GoalType.DeviceScreenTimeGoal -> manageDeviceScreenTimeGoalType(goal)
                GoalType.AppScreenTimeGoal -> manageAppScreenTimeGoalType(goal)
                GoalType.NumeralGoal -> goal
            }
        }
        goalsDao.insertGoals(goals)
    }

    private suspend fun manageTasksGoalType(goal: GoalDbObject): GoalDbObject =
        when (goal.goalInterval) {
            GoalInterval.Daily -> {
                val today = WeekUtils.currentDayOfWeek().isoDayNumber
                val todayTasks = getGroupedTasksStats.dailyTasks(dayIsoNumber = today).first()
                goal.copy(currentValue = todayTasks.completedTasksCount.toLong())
            }
            GoalInterval.Weekly -> {
                if (goal.daysOfWeekSelected.isEmpty() || goal.daysOfWeekSelected.size == 7) {
                    val weeklyTasks = getGroupedTasksStats.weeklyTasks().first()
                    goal.copy(
                        currentValue = weeklyTasks.entries
                            .sumOf { it.value.completedTasksCount }
                            .toLong()
                    )
                } else {
                    val data = goal.daysOfWeekSelected.map { day ->
                        getGroupedTasksStats.dailyTasks(dayIsoNumber = day.isoDayNumber).first()
                    }
                    goal.copy(currentValue = data.sumOf { it.completedTasksCount }.toLong())
                }
            }
            GoalInterval.Custom -> {
                val timeInterval = goal.timeInterval
                if (timeInterval != null) {
                    val tasks = getGroupedTasksStats.timeRangeTasks(timeInterval).first()
                    goal.copy(currentValue = tasks.completedTasksCount.toLong())
                } else goal
            }
        }

    private suspend fun manageDeviceScreenTimeGoalType(goal: GoalDbObject): GoalDbObject =
        when (goal.goalInterval) {
            GoalInterval.Daily -> {
                val today = WeekUtils.currentDayOfWeek().isoDayNumber
                val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                    weekOffset = 0,
                    dayIsoNumber = today
                )
                val stats = usageDataManager.getUsageStats(
                    startTimeMillis = selectedDayTimeRange.first,
                    endTimeMillis = selectedDayTimeRange.last
                )
                goal.copy(currentValue = stats.appsUsageList.sumOf { it.timeInForeground })
            }
            GoalInterval.Weekly -> {
                if (goal.daysOfWeekSelected.isEmpty() || goal.daysOfWeekSelected.size == 7) {
                    val weekTimeRange = StatisticsTimeUtils.weekTimeInMillisRange()
                    val stats = usageDataManager.getUsageStats(
                        startTimeMillis = weekTimeRange.first,
                        endTimeMillis = weekTimeRange.last
                    )
                    goal.copy(currentValue = stats.appsUsageList.sumOf { it.timeInForeground })
                } else {
                    var screenTime = 0L
                    goal.daysOfWeekSelected.map { day ->
                        val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                            weekOffset = 0,
                            dayIsoNumber = day.isoDayNumber
                        )
                        screenTime += usageDataManager.getUsageStats(
                            startTimeMillis = selectedDayTimeRange.first,
                            endTimeMillis = selectedDayTimeRange.last
                        ).appsUsageList.sumOf { it.timeInForeground }
                    }
                    goal.copy(currentValue = screenTime)
                }
            }
            GoalInterval.Custom -> {
                val timeInterval = goal.timeInterval
                if (timeInterval != null) {
                    val stats = usageDataManager.getUsageStats(
                        timeInterval.first,
                        timeInterval.last
                    )
                    goal.copy(currentValue = stats.appsUsageList.sumOf { it.timeInForeground })
                } else goal
            }
        }

    private suspend fun manageAppScreenTimeGoalType(goal: GoalDbObject): GoalDbObject =
        when (goal.goalInterval) {
            GoalInterval.Daily -> {
                val today = WeekUtils.currentDayOfWeek().isoDayNumber
                val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                    weekOffset = 0,
                    dayIsoNumber = today
                )
                var screenTime = 0L
                goal.relatedApps.forEach { packageName ->
                    screenTime += usageDataManager.getAppUsage(
                        startTimeMillis = selectedDayTimeRange.first,
                        endTimeMillis = selectedDayTimeRange.last,
                        packageName = packageName
                    ).timeInForeground
                }
                goal.copy(currentValue = screenTime)
            }
            GoalInterval.Weekly -> {
                if (goal.daysOfWeekSelected.isEmpty() || goal.daysOfWeekSelected.size == 7) {
                    val weekTimeRange = StatisticsTimeUtils.weekTimeInMillisRange()
                    var screenTime = 0L
                    goal.relatedApps.forEach { packageName ->
                        screenTime += usageDataManager.getAppUsage(
                            startTimeMillis = weekTimeRange.first,
                            endTimeMillis = weekTimeRange.last,
                            packageName = packageName
                        ).timeInForeground
                    }
                    goal.copy(currentValue = screenTime)
                } else {
                    var screenTime = 0L
                    goal.daysOfWeekSelected.map { day ->
                        val selectedDayTimeRange = StatisticsTimeUtils.selectedDayTimeInMillisRange(
                            weekOffset = 0,
                            dayIsoNumber = day.isoDayNumber
                        )
                        goal.relatedApps.forEach { packageName ->
                            screenTime += usageDataManager.getAppUsage(
                                startTimeMillis = selectedDayTimeRange.first,
                                endTimeMillis = selectedDayTimeRange.last,
                                packageName = packageName
                            ).timeInForeground
                        }
                    }
                    goal.copy(currentValue = screenTime)
                }
            }
            GoalInterval.Custom -> {
                val timeInterval = goal.timeInterval
                if (timeInterval != null) {
                    var screenTime = 0L
                    goal.relatedApps.forEach { packageName ->
                        screenTime += usageDataManager.getAppUsage(
                            startTimeMillis = timeInterval.first,
                            endTimeMillis = timeInterval.last,
                            packageName = packageName
                        ).timeInForeground
                    }
                    goal.copy(currentValue = screenTime)
                } else goal
            }
        }
}