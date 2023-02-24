package work.racka.reluct.common.network.sync.tasks

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import racka.reluct.common.authentication.managers.ManageUser
import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject
import work.racka.reluct.common.model.enums.responses.NetworkPushResponse
import work.racka.reluct.common.network.model.toNetworkObject
import work.racka.reluct.common.network.util.Constants

internal class FirebaseTasksUpload(
    database: FirebaseDatabase,
    private val manageUser: ManageUser,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksUpload {

    private val tasksRef = database.reference.child(Constants.FB_TASKS)
    private val tasksLabelsRef = database.reference.child(Constants.FB_TASKS_LABELS)

    override suspend fun uploadTasks(tasks: List<TaskDbObject>): NetworkPushResponse =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    val mapped = tasks.map { it.toNetworkObject() }.associateBy { it.id }
                    tasksRef.child(user.id).setValue(mapped)
                    NetworkPushResponse.Success
                } catch (e: Exception) {
                    NetworkPushResponse.Error(e.localizedMessage)
                }
            } else {
                NetworkPushResponse.Unauthorized
            }
        }

    override suspend fun uploadTask(task: TaskDbObject): NetworkPushResponse =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    val mapped = task.toNetworkObject()
                    tasksRef.child(user.id).child(task.id).setValue(mapped)
                    Result.success(Unit)
                    NetworkPushResponse.Success
                } catch (e: Exception) {
                    NetworkPushResponse.Error(e.localizedMessage)
                }
            } else {
                NetworkPushResponse.Unauthorized
            }
        }

    override suspend fun deleteTask(taskId: String): NetworkPushResponse =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    tasksRef.child(user.id).child(taskId).removeValue()
                    NetworkPushResponse.Success
                } catch (e: Exception) {
                    NetworkPushResponse.Error(e.localizedMessage)
                }
            } else {
                NetworkPushResponse.Unauthorized
            }
        }

    override suspend fun uploadLabels(labels: List<TaskLabelDbObject>): NetworkPushResponse =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    val mapped = labels.map { it.toNetworkObject() }.associateBy { it.id }
                    tasksLabelsRef.child(user.id).setValue(mapped)
                    NetworkPushResponse.Success
                } catch (e: Exception) {
                    NetworkPushResponse.Error(e.localizedMessage)
                }
            } else {
                NetworkPushResponse.Unauthorized
            }
        }

    override suspend fun uploadLabel(label: TaskLabelDbObject): NetworkPushResponse =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    val mapped = label.toNetworkObject()
                    tasksLabelsRef.child(user.id).child(label.id).setValue(mapped)
                    NetworkPushResponse.Success
                } catch (e: Exception) {
                    NetworkPushResponse.Error(e.localizedMessage)
                }
            } else {
                NetworkPushResponse.Unauthorized
            }
        }

    override suspend fun deleteLabel(labelId: String): NetworkPushResponse =
        withContext(dispatcher) {
            val user = manageUser.getAuthUser()
            if (user != null) {
                try {
                    tasksLabelsRef.child(user.id).child(labelId).removeValue()
                    NetworkPushResponse.Success
                } catch (e: Exception) {
                    NetworkPushResponse.Error(e.localizedMessage)
                }
            } else {
                NetworkPushResponse.Unauthorized
            }
        }
}
