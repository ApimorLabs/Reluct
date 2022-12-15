package work.racka.reluct.common.network.sync.tasks

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject
import work.racka.reluct.common.network.util.Constants

internal class FirebaseTasksUpload(
    database: FirebaseDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksUpload {

    private val tasksRef = database.reference.child(Constants.FB_TASKS)
    private val tasksLabelsRef = database.reference.child(Constants.FB_TASKS_LABELS)

    override suspend fun uploadTasks(userId: String, tasks: List<TaskDbObject>): Result<Unit> =
        withContext(dispatcher) {
            try {
                val mapped = tasks.associateBy { it.id }
                tasksRef.child(userId).setValue(mapped).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun uploadTask(userId: String, task: TaskDbObject): Result<Unit> =
        withContext(dispatcher) {
            try {
                tasksRef.child(userId).child(task.id).setValue(task).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteTask(userId: String, taskId: String): Result<Unit> =
        withContext(dispatcher) {
            try {
                tasksRef.child(userId).child(taskId).removeValue().await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun uploadLabels(
        userId: String,
        labels: List<TaskLabelDbObject>
    ): Result<Unit> = withContext(dispatcher) {
        try {
            val mapped = labels.associateBy { it.id }
            tasksLabelsRef.child(userId).setValue(mapped).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadLabel(userId: String, label: TaskLabelDbObject): Result<Unit> =
        withContext(dispatcher) {
            try {
                tasksLabelsRef.child(userId).child(label.id).setValue(label).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun deleteLabel(userId: String, labelId: String): Result<Unit> =
        withContext(dispatcher) {
            try {
                tasksLabelsRef.child(userId).child(labelId).removeValue().await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
