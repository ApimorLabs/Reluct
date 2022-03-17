package work.racka.reluct.common.features.tasks.add_edit_task.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.model.data.local.task.TaskDbObject
import work.racka.reluct.common.model.domain.tasks.EditTask

internal class AddEditTaskRepositoryImpl(
    private val dao: TasksDao,
    private val backgroundDispatcher: CoroutineDispatcher
) : AddEditTaskRepository {
    override suspend fun addTask(task: EditTask) =
        withContext(backgroundDispatcher) {
            dao.insertTask(task)
        }

    override suspend fun getTaskToEdit(taskId: Long): Flow<EditTask?> =
        withContext(backgroundDispatcher) {
            dao.getTask(taskId)
                .map { value: TaskDbObject? ->
                    if (value == null) {
                        null
                    } else {
                        EditTask(
                            id = value.id,
                            title = value.title,
                            description = value.description,
                            done = value.done,
                            overdue = value.overdue,
                            dueDateLocalDateTime = value.dueDateLocalDateTime,
                            completedLocalDateTime = value.completedLocalDateTime,
                            reminderLocalDateTime = value.reminderLocalDateTime,
                            timeZoneId = value.timeZoneId
                        )
                    }
                }
        }
}