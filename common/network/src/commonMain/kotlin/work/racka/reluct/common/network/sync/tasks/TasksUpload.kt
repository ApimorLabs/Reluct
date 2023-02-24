package work.racka.reluct.common.network.sync.tasks

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject

interface TasksUpload {
    suspend fun uploadTasks(tasks: List<TaskDbObject>): Result<Unit>
    suspend fun uploadTask(task: TaskDbObject): Result<Unit>
    suspend fun deleteTask(taskId: String): Result<Unit>

    // Labels
    suspend fun uploadLabels(labels: List<TaskLabelDbObject>): Result<Unit>
    suspend fun uploadLabel(label: TaskLabelDbObject): Result<Unit>
    suspend fun deleteLabel(labelId: String): Result<Unit>
}
