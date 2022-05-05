package work.racka.reluct.common.features.tasks.usecases.interfaces

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.Task

internal interface GetWeeklyTasksUseCase {
    /**
     * We need to return a list with each item containing a Pair with the
     * dueDateLocalDateTime from [TaskDbObject] as the first and the relevant [Task] as the second
     */
    operator fun invoke(weekOffset: Int = 0): Flow<List<Pair<String, Task>>>
}