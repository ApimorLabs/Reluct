package work.racka.reluct.common.network.sync.tasks

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject
import work.racka.reluct.common.model.enums.responses.NetworkPushResponse

interface TasksUpload {
    suspend fun uploadTasks(tasks: List<TaskDbObject>): NetworkPushResponse
    suspend fun uploadTask(task: TaskDbObject): NetworkPushResponse
    suspend fun deleteTask(taskId: String): NetworkPushResponse

    // Labels
    suspend fun uploadLabels(labels: List<TaskLabelDbObject>): NetworkPushResponse
    suspend fun uploadLabel(label: TaskLabelDbObject): NetworkPushResponse
    suspend fun deleteLabel(labelId: String): NetworkPushResponse
}
