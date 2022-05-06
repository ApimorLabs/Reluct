package work.racka.reluct.common.features.tasks.usecases.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetDailyTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetWeeklyTasksUseCase
import work.racka.reluct.common.model.domain.tasks.DailyTasksStats
import work.racka.reluct.common.model.util.time.Week

internal class GetDailyTasksUseCaseImpl(
    private val weeklyTasks: GetWeeklyTasksUseCase,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetDailyTasksUseCase {

    override fun invoke(weekOffset: Int, dayOfWeek: Week): Flow<DailyTasksStats> =
        weeklyTasks(weekOffset).map { mapOfWeeklyTasks ->
            mapOfWeeklyTasks[dayOfWeek] ?: DailyTasksStats()
        }.flowOn(backgroundDispatcher)
}