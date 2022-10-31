package work.racka.reluct.common.database.dao.goals

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.database.models.GoalDbObject

interface GoalsDao {
    suspend fun insertGoal(goal: GoalDbObject)

    suspend fun insertGoals(goals: List<GoalDbObject>)

    fun getGoalById(id: String): Flow<GoalDbObject?>

    /**
     * Gets the Goal by its ID synchronously
     */
    suspend fun getGoalByIdSync(id: String): GoalDbObject?

    /**
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     **/
    fun getAllGoals(factor: Long, limitBy: Long = 10): Flow<List<GoalDbObject>>

    /**
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     **/
    fun getActiveGoals(factor: Long, limitBy: Long = 10): Flow<List<GoalDbObject>>

    /**
     * [limitBy] is for how much limit is applied in query. Default is 10
     * [limitBy] * [factor] produces required limit applied in query
     **/
    fun getInActiveGoals(factor: Long, limitBy: Long = 10): Flow<List<GoalDbObject>>

    suspend fun toggleGoalActiveState(id: String, isActive: Boolean)

    suspend fun updateGoalCurrentValue(id: String, currentValue: Long)

    suspend fun deleteGoal(id: String)

    suspend fun deleteAllInActiveGoals(id: String)

    suspend fun deleteAllActiveGoals(id: String)

    suspend fun deleteAllGoals()
}
