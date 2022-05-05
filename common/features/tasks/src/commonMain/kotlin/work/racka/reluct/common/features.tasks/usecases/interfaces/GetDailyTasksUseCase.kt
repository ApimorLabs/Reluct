package work.racka.reluct.common.features.tasks.usecases.interfaces

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

internal interface GetDailyTasksUseCase {
    fun getDailyCompletedTasks(weekOffset: Int = 0, dayIsoNumber: Int): Flow<List<Task>>
    fun getDailyPendingTasks(weekOffset: Int = 0, dayIsoNumber: Int): Flow<List<Task>>
}