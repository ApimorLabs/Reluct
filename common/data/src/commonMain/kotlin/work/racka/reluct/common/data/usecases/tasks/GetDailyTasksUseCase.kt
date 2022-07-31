package work.racka.reluct.common.data.usecases.tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats

interface GetDailyTasksUseCase {
    suspend fun invoke(weekOffset: Int = 0, dayIsoNumber: Int): Flow<DailyTasksStats>
}