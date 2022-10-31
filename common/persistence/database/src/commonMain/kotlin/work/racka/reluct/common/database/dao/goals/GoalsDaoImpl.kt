package work.racka.reluct.common.database.dao.goals

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import work.racka.reluct.common.database.dao.DatabaseWrapper
import work.racka.reluct.common.database.dao.goals.GoalsHelpers.insertAllGoalsToDb
import work.racka.reluct.common.database.dao.goals.GoalsHelpers.insertGoalToDb
import work.racka.reluct.common.database.models.GoalDbObject

internal class GoalsDaoImpl(
    private val dispatcher: CoroutineDispatcher,
    databaseWrapper: DatabaseWrapper
) : GoalsDao {

    private val goalsTableQueries = databaseWrapper.instance?.goalsTableQueries

    override suspend fun insertGoal(goal: GoalDbObject) {
        goalsTableQueries?.insertGoalToDb(goal)
    }

    override suspend fun insertGoals(goals: List<GoalDbObject>) {
        goalsTableQueries?.insertAllGoalsToDb(goals)
    }

    override fun getGoalById(id: String): Flow<GoalDbObject?> = goalsTableQueries
        ?.getGoalById(id, mapper = GoalsHelpers.goalDbObjectMapper)
        ?.asFlow()?.mapToOneOrNull(dispatcher) ?: flowOf(null)

    override suspend fun getGoalByIdSync(id: String): GoalDbObject? = goalsTableQueries
        ?.getGoalById(id, mapper = GoalsHelpers.goalDbObjectMapper)
        ?.executeAsOneOrNull()

    override fun getAllGoals(factor: Long, limitBy: Long): Flow<List<GoalDbObject>> =
        goalsTableQueries
            ?.getAllGoals(
                factor = factor,
                limitBy = limitBy,
                mapper = GoalsHelpers.goalDbObjectMapper
            )
            ?.asFlow()?.mapToList(dispatcher) ?: flowOf(listOf())

    override fun getActiveGoals(factor: Long, limitBy: Long): Flow<List<GoalDbObject>> =
        goalsTableQueries
            ?.getActiveGoals(
                factor = factor,
                limitBy = limitBy,
                mapper = GoalsHelpers.goalDbObjectMapper
            )
            ?.asFlow()?.mapToList(dispatcher) ?: flowOf(listOf())

    override fun getInActiveGoals(factor: Long, limitBy: Long): Flow<List<GoalDbObject>> =
        goalsTableQueries
            ?.getInActiveGoals(
                factor = factor,
                limitBy = limitBy,
                mapper = GoalsHelpers.goalDbObjectMapper
            )
            ?.asFlow()?.mapToList(dispatcher) ?: flowOf(listOf())

    override suspend fun toggleGoalActiveState(id: String, isActive: Boolean) {
        goalsTableQueries?.toggleGoalActive(isActive = isActive, id = id)
    }

    override suspend fun updateGoalCurrentValue(id: String, currentValue: Long) {
        goalsTableQueries?.updateGoalCurrentValue(currentValue = currentValue, id = id)
    }

    override suspend fun deleteGoal(id: String) {
        goalsTableQueries?.deleteGoal(id)
    }

    override suspend fun deleteAllInActiveGoals(id: String) {
        goalsTableQueries?.deleteAllInActiveGoals()
    }

    override suspend fun deleteAllActiveGoals(id: String) {
        goalsTableQueries?.deleteAllActiveGoals()
    }

    override suspend fun deleteAllGoals() {
        goalsTableQueries?.deleteAllGoals()
    }
}
