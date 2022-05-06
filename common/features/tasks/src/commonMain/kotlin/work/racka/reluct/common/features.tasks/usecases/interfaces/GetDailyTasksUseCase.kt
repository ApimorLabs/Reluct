package work.racka.reluct.common.features.tasks.usecases.interfaces

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats

internal interface GetDailyTasksUseCase {
    operator fun invoke(weekOffset: Int = 0, dayIsoNumber: Int): Flow<DailyTasksStats>
}