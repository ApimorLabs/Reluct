package work.racka.reluct.common.features.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import work.racka.reluct.common.features.tasks.statistics.TasksStatistics
import work.racka.reluct.common.features.tasks.statistics.TasksStatisticsImpl
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetDailyTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.GetWeeklyTasksUseCase
import work.racka.reluct.common.features.tasks.usecases.interfaces.ModifyTaskUseCase

actual class TasksStatisticsViewModel internal constructor(
    modifyTasksUsesCase: ModifyTaskUseCase,
    getWeeklyTasksUseCase: GetWeeklyTasksUseCase,
    getDailyTasksUseCase: GetDailyTasksUseCase,
) : ViewModel() {
    actual val host: TasksStatistics = TasksStatisticsImpl(
        modifyTasksUsesCase = modifyTasksUsesCase,
        getWeeklyTasksUseCase = getWeeklyTasksUseCase,
        getDailyTasksUseCase = getDailyTasksUseCase,
        scope = viewModelScope
    )
}