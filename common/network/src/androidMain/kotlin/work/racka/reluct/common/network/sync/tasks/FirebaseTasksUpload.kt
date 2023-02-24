package work.racka.reluct.common.network.sync.tasks

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import racka.reluct.common.authentication.managers.ManageUser
import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject
import work.racka.reluct.common.network.model.toNetworkObject
import work.racka.reluct.common.network.util.Constants

internal class FirebaseTasksUpload(
    database: FirebaseDatabase,
    private val manageUser: ManageUser,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksUpload {

    private val tasksRef = database.reference.child(Constants.FB_TASKS)
    private val tasksLabelsRef = database.reference.child(Constants.FB_TASKS_LABELS)

    override suspend fun uploadTasks(tasks: List<TaskDbObject>): Result<Unit> =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    val mapped = tasks.map { it.toNetworkObject() }.associateBy { it.id }
                    tasksRef.child(user.id).setValue(mapped).await()
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            } else {
                Result.failure(Exception("Unauthorized"))
            }
        }

    override suspend fun uploadTask(task: TaskDbObject): Result<Unit> =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    val mapped = task.toNetworkObject()
                    tasksRef.child(user.id).child(task.id).setValue(mapped).await()
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            } else {
                Result.failure(Exception("Unauthorized"))
            }
        }

    override suspend fun deleteTask(taskId: String): Result<Unit> =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    tasksRef.child(user.id).child(taskId).removeValue().await()
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            } else {
                Result.failure(Exception("Unauthorized"))
            }
        }

    override suspend fun uploadLabels(labels: List<TaskLabelDbObject>): Result<Unit> =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    val mapped = labels.map { it.toNetworkObject() }.associateBy { it.id }
                    tasksLabelsRef.child(user.id).setValue(mapped).await()
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            } else {
                Result.failure(Exception("Unauthorized"))
            }
        }

    override suspend fun uploadLabel(label: TaskLabelDbObject): Result<Unit> =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    val mapped = label.toNetworkObject()
                    tasksLabelsRef.child(user.id).child(label.id).setValue(mapped).await()
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            } else {
                Result.failure(Exception("Unauthorized"))
            }
        }

    override suspend fun deleteLabel(labelId: String): Result<Unit> =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    tasksLabelsRef.child(user.id).child(labelId).removeValue().await()
                    Result.success(Unit)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            } else {
                Result.failure(Exception("Unauthorized"))
            }
        }
}
