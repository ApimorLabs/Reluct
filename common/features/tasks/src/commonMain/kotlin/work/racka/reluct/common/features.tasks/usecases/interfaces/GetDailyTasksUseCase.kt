package work.racka.reluct.common.features.tasks.usecases.interfaces

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

internal interface GetDailyTasksUseCase {
    operator fun invoke(weekOffset: Int = 0, dayOfWeek: Week): Flow<DailyTasksStats>
}