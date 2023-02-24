package work.racka.reluct.common.network.sync.tasks

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject
import work.racka.reluct.common.model.enums.responses.NetworkPushResponse

internal class DesktopTasksUpload : TasksUpload {
    override suspend fun uploadTasks(tasks: List<TaskDbObject>): NetworkPushResponse {
        return NetworkPushResponse.Unauthorized
    }

    override suspend fun uploadTask(task: TaskDbObject): NetworkPushResponse {
        return NetworkPushResponse.Unauthorized
    }

    override suspend fun deleteTask(taskId: String): NetworkPushResponse {
        return NetworkPushResponse.Unauthorized
    }

    override suspend fun uploadLabels(labels: List<TaskLabelDbObject>): NetworkPushResponse {
        return NetworkPushResponse.Unauthorized
    }

    override suspend fun uploadLabel(label: TaskLabelDbObject): NetworkPushResponse {
        return NetworkPushResponse.Unauthorized
    }

    override suspend fun deleteLabel(labelId: String): NetworkPushResponse {
        return NetworkPushResponse.Unauthorized
    }
}
