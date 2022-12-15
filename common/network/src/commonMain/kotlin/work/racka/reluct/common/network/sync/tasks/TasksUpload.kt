package work.racka.reluct.common.network.sync.tasks

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject

interface TasksUpload {
    suspend fun uploadTasks(userId: String, tasks: List<TaskDbObject>): Result<Unit>
    suspend fun uploadTask(userId: String, task: TaskDbObject): Result<Unit>
    suspend fun deleteTask(userId: String, taskId: String): Result<Unit>

    // Labels
    suspend fun uploadLabels(userId: String, labels: List<TaskLabelDbObject>): Result<Unit>
    suspend fun uploadLabel(userId: String, label: TaskLabelDbObject): Result<Unit>
    suspend fun deleteLabel(userId: String, labelId: String): Result<Unit>
}
