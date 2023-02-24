package work.racka.reluct.common.network.sync

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import racka.reluct.common.authentication.managers.ManageUser
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.network.model.TaskLabelNetworkObject
import work.racka.reluct.common.network.model.TaskNetworkObject
import work.racka.reluct.common.network.util.Constants

internal class FirebaseNetworkSyncService(
    database: FirebaseDatabase,
    private val manageUser: ManageUser,
    private val tasksDao: TasksDao
) : DbNetworkSync {

    private var currentUserId: String? = null
    private val tasksRef = database.reference.child(Constants.FB_TASKS).also { it.keepSynced(true) }
    private val tasksLabelsRef = database.reference.child(Constants.FB_TASKS_LABELS)
        .also { it.keepSynced(true) }

    private val tasksListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val tasks = snapshot.children.mapNotNull { taskSnapshot ->
                    taskSnapshot.getValue<TaskNetworkObject>()?.toDbObject()
                }
                tasksDao.replaceTasks(tasks)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            println("Tasks Listener Cancelled: ${error.message}")
        }
    }

    private val tasksLabelsListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val taskLabels = snapshot.children.mapNotNull { taskSnapshot ->
                    taskSnapshot.getValue<TaskLabelNetworkObject>()?.toDbObject()
                }
                tasksDao.addAllTaskLabels(taskLabels)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            println("Tasks Labels Listener Cancelled: ${error.message}")
        }
    }

    override fun syncTasksData() {
        val user = manageUser.getAuthUser()
        if (user != null) {
            currentUserId = user.id
            currentUserId?.let { id ->
                // Sync Tasks from Network
                tasksRef.child(id).addValueEventListener(tasksListener)
                // Sync Tasks Labels from Network
                tasksLabelsRef.child(id).addValueEventListener(tasksLabelsListener)
            }
        }
    }

    override fun stopSyncTasksData() {
        currentUserId?.let { id ->
            tasksRef.child(id).addValueEventListener(tasksListener)
            tasksLabelsRef.child(id).addValueEventListener(tasksLabelsListener)
        }
    }
}
