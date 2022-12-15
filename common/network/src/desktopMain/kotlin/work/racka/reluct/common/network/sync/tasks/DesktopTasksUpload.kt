package work.racka.reluct.common.network.sync.tasks

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject

internal class DesktopTasksUpload : TasksUpload {
    override suspend fun uploadTasks(userId: String, tasks: List<TaskDbObject>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun uploadTask(userId: String, task: TaskDbObject): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteTask(userId: String, taskId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun uploadLabels(
        userId: String,
        labels: List<TaskLabelDbObject>
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun uploadLabel(userId: String, label: TaskLabelDbObject): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteLabel(userId: String, labelId: String): Result<Unit> {
        return Result.success(Unit)
    }
}
