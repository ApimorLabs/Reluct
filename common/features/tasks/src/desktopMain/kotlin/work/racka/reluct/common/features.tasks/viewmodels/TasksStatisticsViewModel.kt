package work.racka.reluct.common.features.tasks.viewmodels

import kotlinx.coroutines.CoroutineScope
import work.racka.reluct.common.data.usecases.tasks.GetDailyTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.GetWeeklyTasksUseCase
import work.racka.reluct.common.data.usecases.tasks.ModifyTaskUseCase
import work.racka.reluct.common.data.usecases.time.GetWeekRangeFromOffset
import work.racka.reluct.common.features.tasks.statistics.TasksStatistics
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsImpl

actual class TasksStatisticsViewModel internal constructor(
    modifyTasksUsesCase: ModifyTaskUseCase,
    getWeeklyTasksUseCase: GetWeeklyTasksUseCase,
    getDailyTasksUseCase: GetDailyTasksUseCase,
    getWeekRangeFromOffset: GetWeekRangeFromOffset,
    scope: CoroutineScope,
) {
    actual val host: TasksStatistics = TasksStatisticsImpl(
        modifyTasksUsesCase = modifyTasksUsesCase,
        getWeeklyTasksUseCase = getWeeklyTasksUseCase,
        getDailyTasksUseCase = getDailyTasksUseCase,
        getWeekRangeFromOffset = getWeekRangeFromOffset,
        scope = scope
    )
}