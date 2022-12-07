package work.racka.reluct.common.database.sync

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.database.models.TaskDbObject
import work.racka.reluct.common.database.models.TaskLabelDbObject

internal class FirebaseNetworkSyncService(
    database: FirebaseDatabase,
    private val tasksDao: TasksDao
) : DbNetworkSync {

    private var currentUserId: String? = null
    private val tasksRef = database.reference.child("tasks")
    private val tasksLabelsRef = database.reference.child("tasks_labels")

    private val tasksListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                val tasks = snapshot.children.mapNotNull { taskSnapshot ->
                    taskSnapshot.getValue<TaskDbObject>()
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
                    taskSnapshot.getValue<TaskLabelDbObject>()
                }
                tasksDao.addAllTaskLabels(taskLabels)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            println("Tasks Labels Listener Cancelled: ${error.message}")
        }
    }

    override fun syncTasksData(userId: String) {
        currentUserId = userId
        currentUserId?.let { id ->
            // Sync Tasks from Network
            tasksRef.child(id).addValueEventListener(tasksListener)
            // Sync Tasks Labels from Network
            tasksLabelsRef.child(id).addValueEventListener(tasksLabelsListener)
        }
    }

    override fun stopSyncTasksData() {
        currentUserId?.let { id ->
            tasksRef.child(id).addValueEventListener(tasksListener)
            tasksLabelsRef.child(id).addValueEventListener(tasksLabelsListener)
        }
    }
}
