package work.racka.reluct.common.domain.usecases.goals

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.goals.Goal

interface GetGoals {
    fun getActiveGoals(factor: Long, limitBy: Long = 10): Flow<ImmutableList<Goal>>
    fun getInActiveGoals(factor: Long, limitBy: Long = 10): Flow<ImmutableList<Goal>>
    fun getGoal(id: String): Flow<Goal?>
    suspend fun getGoalSync(id: String): Goal?
}
