package work.racka.reluct.common.data.usecases.goals.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import work.racka.reluct.common.data.mappers.goals.asGoal
import work.racka.reluct.common.data.usecases.app_info.GetAppInfo
import work.racka.reluct.common.data.usecases.goals.GetGoals
import work.racka.reluct.common.database.dao.goals.GoalsDao
import work.racka.reluct.common.model.domain.goals.Goal

@OptIn(ExperimentalCoroutinesApi::class)
class GetGoalsImpl(
    private val goalsDao: GoalsDao,
    private val getAppInfo: GetAppInfo,
    private val backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetGoals {
    override fun getActiveGoals(factor: Long, limitBy: Long): Flow<List<Goal>> = goalsDao
        .getActiveGoals(factor = factor, limitBy = limitBy)
        .mapLatest { list -> list.map { it.asGoal(getAppInfo) }.asReversed() }
        .flowOn(backgroundDispatcher)

    override fun getInActiveGoals(factor: Long, limitBy: Long): Flow<List<Goal>> = goalsDao
        .getInActiveGoals(factor = factor, limitBy = limitBy)
        .mapLatest { list -> list.map { it.asGoal(getAppInfo) }.asReversed() }
        .flowOn(backgroundDispatcher)

    override fun getGoal(id: String): Flow<Goal?> = goalsDao.getGoalById(id)
        .mapLatest { it?.asGoal(getAppInfo) }
        .flowOn(backgroundDispatcher)

    override suspend fun getGoalSync(id: String): Goal? = goalsDao.getGoalByIdSync(id)
        ?.asGoal(getAppInfo)
}