package work.racka.reluct.widgets.tasks.actions

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.common.domain.usecases.tasks.GetTasksUseCase
import work.racka.reluct.widgets.tasks.PendingTasksWidget
import work.racka.reluct.widgets.tasks.state.PendingTasksInfo
import work.racka.reluct.widgets.tasks.state.PendingTasksStateDefinition
import work.racka.reluct.widgets.tasks.state.asWidgetTaskParcel

object PendingTasksSource : KoinComponent {

    private val context: Context by inject()
    private val coroutineScope = MainScope()

    private val getTasks: GetTasksUseCase by inject()
    private var loadJob: Job? = null

    fun initialize() {
        loadJob?.cancel()
        loadJob = coroutineScope.launch {
            loadData()
        }
    }

    private suspend fun loadData() {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(PendingTasksWidget::class.java)
        setWidgetState(glanceIds, PendingTasksInfo.Loading)
        val pending = getTasks.getPendingTasks(1L).first()
            .groupBy { it.dueDate }
            .map { sourceMap ->
                val newList = sourceMap.value.map { it.asWidgetTaskParcel() }
                Pair(sourceMap.key, newList)
            }.toMap()
        if (pending.isEmpty()) {
            setWidgetState(glanceIds, PendingTasksInfo.Nothing)
        } else {
            setWidgetState(glanceIds, PendingTasksInfo.Data(pending))
        }
    }

    private suspend fun setWidgetState(glanceIds: List<GlanceId>, newState: PendingTasksInfo) {
        glanceIds.forEach { id ->
            updateAppWidgetState(
                context = context,
                definition = PendingTasksStateDefinition,
                glanceId = id,
                updateState = { newState }
            )
        }
        PendingTasksWidget().updateAll(context)
    }
}
