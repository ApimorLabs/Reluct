package work.racka.reluct.common.data.usecases.goals.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.mappers.goals.asGoalDbObject
import work.racka.reluct.common.data.usecases.goals.ModifyGoals
import work.racka.reluct.common.data.usecases.tasks.GetGroupedTasksStats
import work.racka.reluct.common.database.dao.goals.GoalsDao
import work.racka.reluct.common.database.models.GoalDbObject
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.time.WeekUtils
import work.racka.reluct.common.system_service.haptics.HapticFeedback

class ModifyGoalsImpl(
    private val goalsDao: GoalsDao,
    private val haptics: HapticFeedback,
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
        val activeGoals = goalsDao.getActiveGoals(factor = 0).first()
        activeGoals.map { goal ->
            when (goal.goalType) {
                GoalType.TasksGoal -> manageTasksGoalType(goal)
                GoalType.DeviceScreenTimeGoal -> {}
                else -> {}
            }
        }
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

    private fun manageDeviceScreenTimeGoalType(goal: GoalDbObject) {

    }
}