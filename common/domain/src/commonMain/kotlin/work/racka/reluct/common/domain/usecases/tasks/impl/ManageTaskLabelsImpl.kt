package work.racka.reluct.common.domain.usecases.tasks.impl

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
import work.racka.reluct.common.system_service.haptics.HapticFeedback

internal class ManageTaskLabelsImpl(
    private val dao: TasksDao,
    private val haptics: HapticFeedback,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ManageTaskLabels {

    override suspend fun addLabel(label: TaskLabel) = withContext(dispatcher) {
        dao.addTaskLabel(label = label.asTaskLabelDbObject())
        haptics.click()
    }

    override suspend fun adLabels(labels: List<TaskLabel>) = withContext(dispatcher) {
        dao.addAllTaskLabels(labels = labels.map { it.asTaskLabelDbObject() })
        haptics.click()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun allLabels(): Flow<List<TaskLabel>> = dao.getAllTaskLabels()
        .mapLatest { list -> list.map { it.asTaskLabel() } }
        .flowOn(dispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getLabel(id: String): Flow<TaskLabel?> = dao.getTaskLabel(id)
        .mapLatest { item -> item?.asTaskLabel() }
        .flowOn(dispatcher)

    override suspend fun deleteLabel(id: String) = withContext(dispatcher) {
        dao.deleteTaskLabel(id)
        haptics.heavyClick()
    }

    override suspend fun deleteAllLabels() = withContext(dispatcher) {
        dao.deleteAllTaskLabels()
        haptics.heavyClick()
    }
}