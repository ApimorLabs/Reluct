package work.racka.reluct.common.domain.usecases.tasks

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.model.domain.tasks.TaskLabel

interface ManageTaskLabels {
    suspend fun addLabel(label: TaskLabel)
    suspend fun addLabels(labels: ImmutableList<TaskLabel>)

    fun allLabels(): Flow<ImmutableList<TaskLabel>>
    fun getLabel(id: String): Flow<TaskLabel?>

    suspend fun deleteLabel(id: String)
    suspend fun deleteAllLabels()
}