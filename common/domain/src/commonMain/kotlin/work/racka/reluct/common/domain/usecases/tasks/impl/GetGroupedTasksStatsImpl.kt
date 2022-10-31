package work.racka.reluct.common.domain.usecases.tasks.impl

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.domain.mappers.tasks.asTask
import work.racka.reluct.common.domain.usecases.tasks.GetGroupedTasksStats
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.common.model.util.time.Week

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetGroupedTasksStatsImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetGroupedTasksStats {
    private val daysOfWeek = Week.values()

    private fun daysOfWeekDateTimeRanges(weekOffset: Int) = daysOfWeek.map {
        val range = StatisticsTimeUtils
            .selectedDayDateTimeStringRange(weekOffset = weekOffset, dayIsoNumber = it.isoDayNumber)
        Pair(it, range)
    }.toTypedArray()

    private fun dayDateTimeStringRange(weekOffset: Int, dayIsoNumber: Int) = StatisticsTimeUtils
        .selectedDayDateTimeStringRange(weekOffset = weekOffset, dayIsoNumber = dayIsoNumber)


    override fun dailyTasks(weekOffset: Int, dayIsoNumber: Int): Flow<DailyTasksStats> {
        val dayRange = dayDateTimeStringRange(weekOffset, dayIsoNumber)
        return dao.getTasksBetweenDateTime(
            startLocalDateTime = dayRange.start,
            endLocalDateTime = dayRange.endInclusive
        )
            .mapLatest { list ->
                val pendingTempList = list
                    .filter { !it.done }
                    .map { it.asTask() }
                    .toImmutableList()
                val completedTempList = list
                    .filter { it.done }
                    .map { it.asTask() }
                    .toImmutableList()
                DailyTasksStats(
                    dateFormatted = TimeUtils
                        .getFormattedDateString(dateTime = dayRange.start),
                    completedTasks = completedTempList,
                    pendingTasks = pendingTempList
                )
            }
            .flowOn(backgroundDispatcher)
    }


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
    override fun weeklyTasks(weekOffset: Int): Flow<ImmutableMap<Week, DailyTasksStats>> {
        // Monday to Sunday
        val weeklyTimeRange = StatisticsTimeUtils
            .weekLocalDateTimeStringRange(weekOffset = weekOffset)
        return dao.getTasksBetweenDateTime(
            startLocalDateTime = weeklyTimeRange.start,
            endLocalDateTime = weeklyTimeRange.endInclusive
        )
            .mapLatest { list ->
                /**
                 * Iterate through the time ranges for each day of the week and get the
                 * specific pending and completed tasks for each day that fall within the
                 * respective range. Each day will have it's own [DailyTasksStats] which also
                 * carries the pending and completed tasks lists.
                 */
                /**
                 * Iterate through the time ranges for each day of the week and get the
                 * specific pending and completed tasks for each day that fall within the
                 * respective range. Each day will have it's own [DailyTasksStats] which also
                 * carries the pending and completed tasks lists.
                 */
                daysOfWeekDateTimeRanges(weekOffset).associate { rangePair ->
                    val pendingTempList = list
                        .filter { rangePair.second.contains(it.dueDateLocalDateTime) && !it.done }
                        .map { it.asTask() }
                        .toImmutableList()
                    val completedTempList = list
                        .filter { rangePair.second.contains(it.dueDateLocalDateTime) && it.done }
                        .map { it.asTask() }
                        .toImmutableList()
                    val dailyTasksStats = DailyTasksStats(
                        dateFormatted = TimeUtils
                            .getFormattedDateString(dateTime = rangePair.second.start),
                        completedTasks = completedTempList,
                        pendingTasks = pendingTempList
                    )
                    rangePair.first to dailyTasksStats
                }.toImmutableMap()
            }
            .flowOn(backgroundDispatcher)
    }

    override fun timeRangeTasks(timeRangeMillis: LongRange): Flow<DailyTasksStats> {
        val dateTimeRange = TimeUtils.run {
            epochMillisToLocalDateTime(timeRangeMillis.first).toString()..
                    epochMillisToLocalDateTime(timeRangeMillis.last).toString()
        }
        return dao.getTasksBetweenDateTime(
            startLocalDateTime = dateTimeRange.start,
            endLocalDateTime = dateTimeRange.endInclusive
        )
            .mapLatest { list ->
                val pendingTempList = list
                    .filter { !it.done }
                    .map { it.asTask() }
                    .toImmutableList()
                val completedTempList = list
                    .filter { it.done }
                    .map { it.asTask() }
                    .toImmutableList()
                DailyTasksStats(
                    dateFormatted = TimeUtils
                        .getFormattedDateString(dateTime = dateTimeRange.start),
                    completedTasks = completedTempList,
                    pendingTasks = pendingTempList
                )
            }
            .flowOn(backgroundDispatcher)
    }
}