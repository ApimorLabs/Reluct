package work.racka.reluct.common.features.tasks.usecases.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetDailyTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetWeeklyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils

internal class GetDailyTasksUseCaseImpl(
    private val weeklyTasks: GetWeeklyTasksUseCase,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetDailyTasksUseCase {

    override fun getDailyCompletedTasks(weekOffset: Int, dayIsoNumber: Int): Flow<List<Task>> {
        val selectDayDateTimeRange = StatisticsTimeUtils
            .selectedDayDateTimeStringRange(weekOffset = weekOffset, dayIsoNumber = dayIsoNumber)
        return weeklyTasks(weekOffset).map { weeklyTasks ->
            weeklyTasks.filter { selectDayDateTimeRange.contains(it.first) && it.second.done }
                .map { it.second }
        }.flowOn(backgroundDispatcher)
    }

    override fun getDailyPendingTasks(weekOffset: Int, dayIsoNumber: Int): Flow<List<Task>> {
        val selectDayDateTimeRange = StatisticsTimeUtils
            .selectedDayDateTimeStringRange(weekOffset = weekOffset, dayIsoNumber = dayIsoNumber)
        return weeklyTasks(weekOffset).map { weeklyTasks ->
            weeklyTasks.filter { selectDayDateTimeRange.contains(it.first) && !it.second.done }
                .map { it.second }
        }.flowOn(backgroundDispatcher)
    }
}