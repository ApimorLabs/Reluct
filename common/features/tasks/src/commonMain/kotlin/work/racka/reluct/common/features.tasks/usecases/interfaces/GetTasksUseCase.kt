package work.racka.reluct.common.features.tasks.usecases.interfaces

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

/**
 * Gets a Task or Tasks depending on the user's need
 * It should be used with respective ViewModels to perform actions
 * Implementations found in the impl folder
 */
internal interface GetTasksUseCase {
    fun getPendingTasks(): Flow<List<Task>>
    fun getCompletedTasks(): Flow<List<Task>>
    fun getSearchedTasks(query: String): Flow<List<Task>>
    fun getTask(taskId: String): Flow<Task?>
}