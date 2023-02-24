package work.racka.reluct.common.network.sync.tasks

import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject

internal class DesktopTasksUpload : TasksUpload {
    override suspend fun uploadTasks(tasks: List<TaskDbObject>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun uploadTask(task: TaskDbObject): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun uploadLabels(labels: List<TaskLabelDbObject>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun uploadLabel(label: TaskLabelDbObject): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteLabel(labelId: String): Result<Unit> {
        return Result.success(Unit)
    }
}
