package work.racka.reluct.common.features.tasks.add_edit_task.repository

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.EditTask

interface AddEditTaskRepository {
    suspend fun addTask(task: EditTask)
    suspend fun getTaskToEdit(taskId: Long): Flow<EditTask?>
}