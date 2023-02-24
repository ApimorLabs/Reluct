package work.racka.reluct.common.domain.usecases.tasks.impl

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import work.racka.reluct.common.database.dao.tasks.TasksDao
import work.racka.reluct.common.domain.mappers.tasks.asTaskLabel
import work.racka.reluct.common.domain.mappers.tasks.asTaskLabelDbObject
import work.racka.reluct.common.domain.usecases.tasks.ManageTaskLabels
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.common.model.enums.responses.NetworkPushResponse
import work.racka.reluct.common.network.sync.tasks.TasksUpload
import work.racka.reluct.common.services.haptics.HapticFeedback

internal class ManageTaskLabelsImpl(
    private val dao: TasksDao,
    private val tasksUpload: TasksUpload,
    private val haptics: HapticFeedback,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ManageTaskLabels {

    override suspend fun addLabel(label: TaskLabel) = withContext(dispatcher) {
        val mapped = label.asTaskLabelDbObject()
        val resp = tasksUpload.uploadLabel(mapped)
        if (resp !is NetworkPushResponse.Success) {
            dao.addTaskLabel(label = label.asTaskLabelDbObject())
        }
        haptics.click()
    }

    override suspend fun addLabels(labels: ImmutableList<TaskLabel>) = withContext(dispatcher) {
        val mapped = labels.map { it.asTaskLabelDbObject() }
        val resp = tasksUpload.uploadLabels(mapped)
        if (resp !is NetworkPushResponse.Success) {
            dao.addAllTaskLabels(labels = labels.map { it.asTaskLabelDbObject() })
        }
        haptics.click()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allLabels(): Flow<ImmutableList<TaskLabel>> = dao.getAllTaskLabels()
        .mapLatest { list -> list.map { it.asTaskLabel() }.toImmutableList() }
        .flowOn(dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLabel(id: String): Flow<TaskLabel?> = dao.getTaskLabel(id)
        .mapLatest { item -> item?.asTaskLabel() }
        .flowOn(dispatcher)

    override suspend fun deleteLabel(id: String) = withContext(dispatcher) {
        val resp = tasksUpload.deleteLabel(id)
        if (resp !is NetworkPushResponse.Success) {
            dao.deleteTaskLabel(id)
        }
        haptics.heavyClick()
    }

    override suspend fun deleteAllLabels() = withContext(dispatcher) {
        // TODO: Implement on Firebase too
        dao.deleteAllTaskLabels()
        haptics.heavyClick()
    }
}
