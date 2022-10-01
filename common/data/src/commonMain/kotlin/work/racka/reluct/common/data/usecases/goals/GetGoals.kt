package work.racka.reluct.common.data.usecases.goals

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.goals.Goal

interface GetGoals {
    fun getActiveGoals(factor: Long, limitBy: Long): Flow<List<Goal>>
    fun getInActiveGoals(factor: Long, limitBy: Long): Flow<List<Goal>>
    fun getGoal(id: String): Flow<Goal?>
    suspend fun getGoalSync(id: String): Goal?
}