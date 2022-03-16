package work.racka.reluct.common.features.tasks.task_details

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

interface TaskDetails {
    fun getTask(taskId: Long): Flow<Task?>
}