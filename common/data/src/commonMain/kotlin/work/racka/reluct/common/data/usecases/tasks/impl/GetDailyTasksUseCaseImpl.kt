package work.racka.reluct.common.data.usecases.tasks.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.data.usecases.tasks.GetDailyTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.GetWeeklyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

internal class GetDailyTasksUseCaseImpl(
    private val weeklyTasks: GetWeeklyTasksUseCase,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetDailyTasksUseCase {

    override fun invoke(weekOffset: Int, dayIsoNumber: Int): Flow<DailyTasksStats> =
        weeklyTasks.invoke(weekOffset).map { mapOfWeeklyTasks ->
            val dayOfWeek = Week.values()
                .first { it.isoDayNumber == dayIsoNumber }
            mapOfWeeklyTasks[dayOfWeek] ?: DailyTasksStats()
        }.flowOn(backgroundDispatcher)
}