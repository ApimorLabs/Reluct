package work.racka.reluct.common.data.usecases.tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

interface GetGroupedTasksStats {
    fun dailyTasks(weekOffset: Int = 0, dayIsoNumber: Int): Flow<DailyTasksStats>
    fun weeklyTasks(weekOffset: Int = 0): Flow<Map<Week, DailyTasksStats>>
    fun timeRangeTasks(timeRangeMillis: LongRange): Flow<DailyTasksStats>
}