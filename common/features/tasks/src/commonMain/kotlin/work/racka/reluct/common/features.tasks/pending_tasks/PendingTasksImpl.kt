package work.racka.reluct.common.features.tasks.pending_tasks

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.Task

class PendingTasksImpl : PendingTasks {

    val map = mapOf<String, Int>("string" to 2, "me" to 43)
    fun get() {
        map.forEach {
            it.key
            it.value
        }
    }

    override suspend fun getTasks(): Flow<Map<String, List<Task>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOverdueTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun markAsDone(taskId: Long) {
        TODO("Not yet implemented")
    }
}