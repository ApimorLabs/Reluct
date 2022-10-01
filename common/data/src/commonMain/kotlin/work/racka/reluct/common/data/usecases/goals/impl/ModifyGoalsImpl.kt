package work.racka.reluct.common.data.usecases.goals.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.common.data.mappers.goals.asGoalDbObject
import work.racka.reluct.common.data.usecases.goals.ModifyGoals
import work.racka.reluct.common.database.dao.goals.GoalsDao
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.system_service.haptics.HapticFeedback

class ModifyGoalsImpl(
    private val goalsDao: GoalsDao,
    private val haptics: HapticFeedback,
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
}