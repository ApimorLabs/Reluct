package work.racka.reluct.common.data.usecases.goals

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.goals.Goal

interface GetGoals {
    suspend fun getActiveGoals(factor: Long, limitBy: Long): Flow<List<Goal>>
    suspend fun getInActiveGoals(factor: Long, limitBy: Long): Flow<List<Goal>>
    suspend fun getGoal(): Flow<Goal?>
    suspend fun getGoalSync(): Goal?
}