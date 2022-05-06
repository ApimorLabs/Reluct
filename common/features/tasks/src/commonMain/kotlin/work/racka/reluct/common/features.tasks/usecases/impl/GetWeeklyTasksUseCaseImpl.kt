package work.racka.reluct.common.features.tasks.usecases.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetWeeklyTasksUseCase
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.Week

internal class GetWeeklyTasksUseCaseImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetWeeklyTasksUseCase {

    private val daysOfWeek = Week.values()

    private fun daysOfWeekDateTimeRanges(weekOffset: Int) = daysOfWeek.map {
        val range = StatisticsTimeUtils
            .selectedDayDateTimeStringRange(weekOffset = weekOffset, dayIsoNumber = it.isoDayNumber)
        Pair(it, range)
    }.toTypedArray()

    /**
     * We need to return a map with each item containing the day of the [Week] as key and the
     * [DailyTasksStats] as the values. This map will contain data for 7 days for the specific
     * week determined by [weekOffset]. [weekOffset] = 0 means that it will get the data for the
     * current week. Positive values get data for the coming weeks and Negative values get data for
     * previous weeks.
     * Note: This accounts for the week starting on Monday. For scenarios where you want the week
     * to start on Sunday you would have to provide a different implementation for
     * [StatisticsTimeUtils.weekLocalDateTimeStringRange] and
     * [StatisticsTimeUtils.selectedDayDateTimeStringRange]
     */
    override fun invoke(weekOffset: Int): Flow<Map<Week, DailyTasksStats>> {
        // Monday to Sunday
        val weeklyTimeRange = StatisticsTimeUtils
            .weekLocalDateTimeStringRange(weekOffset = weekOffset)
        // Array with time range for each day of the Week
        val daysOfWeekTimeRanges = daysOfWeekDateTimeRanges(weekOffset)
        return dao.getTasksBetweenDateTime(
            startLocalDateTime = weeklyTimeRange.start,
            endLocalDateTime = weeklyTimeRange.endInclusive)
            .map { list ->
                /**
                 * Iterate through the time ranges for each day of the week and get the
                 * specific pending and completed tasks for each day that fall within the
                 * respective range. Each day will have it's own [DailyTasksStats] which also
                 * carries the pending and completed tasks lists.
                 */
                daysOfWeekTimeRanges.associate { rangePair ->
                    val pendingTempList = list
                        .filter { rangePair.second.contains(it.dueDateLocalDateTime) && !it.done }
                        .map { it.asTask() }
                    val completedTempList = list
                        .filter { rangePair.second.contains(it.dueDateLocalDateTime) && it.done }
                        .map { it.asTask() }
                    val dailyTasksStats = DailyTasksStats(
                        completedTasks = completedTempList,
                        pendingTasks = pendingTempList
                    )
                    rangePair.first to dailyTasksStats
                }
            }
            .flowOn(backgroundDispatcher)
    }
}