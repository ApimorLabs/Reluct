package work.racka.reluct.common.data.usecases.tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

interface GetWeeklyTasksUseCase {
    /**
     * We need to return a map with each item containing the day of the [Week] as key and the
     * [DailyTasksStats] as the values. This map will contain data for 7 days for the specific
     * week determined by [weekOffset]. [weekOffset] = 0 means that it will get the data for the
     * current week. Positive values get data for the coming weeks and Negative values get data for
     * previous weeks.
     */
    suspend fun invoke(weekOffset: Int = 0): Flow<Map<Week, DailyTasksStats>>
}