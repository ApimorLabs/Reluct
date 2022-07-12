package work.racka.reluct.widgets.tasks

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalGlanceId
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.widgets.core.WidgetTheme
import work.racka.reluct.widgets.tasks.actions.ReloadTasksAction

class PendingTasksWidget : GlanceAppWidget(), KoinComponent {

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    private val context: Context by inject()

    private var glanceId by mutableStateOf<GlanceId?>(null)
    private var data by mutableStateOf<Data?>(null)

    private val coroutineScope = MainScope()

    private val getTasks: GetTasksUseCase by inject()

    data class Data(val pending: Map<String, List<Task>>)

    private suspend fun loadData(): Data {
        LocalGlanceId
        val pending = getTasks.getPendingTasks(1L).first()
            .groupBy { it.dueDate }
        return Data(pending = pending)
    }

    fun initiateLoad() {
        coroutineScope.launch {
            data = loadData()
            glanceId?.run {
                update(context, this)
            }
        }.invokeOnCompletion { coroutineScope.cancel() }
    }

    @Composable
    override fun Content() {
        glanceId = LocalGlanceId.current

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .appWidgetBackground()
                .background(WidgetTheme.Colors.background)
                .cornerRadius(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if (data == null) {
                CircularProgressIndicator(
                    GlanceModifier.clickable(
                        actionRunCallback<ReloadTasksAction>()
                    )
                )
            } else PendingTasksList(pendingTasks = data!!.pending)
        }
    }
}