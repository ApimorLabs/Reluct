package work.racka.reluct.common.features.tasks.usecases.interfaces

import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.domain.tasks.Task

internal interface ModifyTasksUseCase {
    suspend fun addTask(task: EditTask)
    suspend fun deleteTask(taskId: String)
    fun toggleTaskDone(task: Task, isDone: Boolean)
}