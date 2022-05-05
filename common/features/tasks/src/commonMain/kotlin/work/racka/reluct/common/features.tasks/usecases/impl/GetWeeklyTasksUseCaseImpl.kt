package work.racka.reluct.common.features.tasks.usecases.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetWeeklyTasksUseCase
import work.racka.reluct.common.features.tasks.util.DataMappers.asTask
import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.util.time.StatisticsTimeUtils

internal class GetWeeklyTasksUseCaseImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher,
) : GetWeeklyTasksUseCase {

    /**
     * We need to return a list with each item containing a Pair with the
     * dueDateLocalDateTime from [TaskDbObject] as the first and the relevant [Task] as the second
     */
    override fun invoke(weekOffset: Int): Flow<List<Pair<String, Task>>> {
        val timeRange = StatisticsTimeUtils
            .weekLocalDateTimeStringRange(weekOffset = weekOffset)
        return dao.getTasksBetweenDateTime(
            startLocalDateTime = timeRange.start,
            endLocalDateTime = timeRange.endInclusive)
            .map { list ->
                list.map { Pair(it.dueDateLocalDateTime, it.asTask()) }
            }
            .flowOn(backgroundDispatcher)
    }
}