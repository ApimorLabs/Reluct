package work.racka.reluct.widgets.tasks

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalGlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task

class PendingTasksWidget : GlanceAppWidget(), KoinComponent {

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    private val context: Context by inject()

    private val manager = GlanceAppWidgetManager(context)

    private var glanceId by mutableStateOf<GlanceId?>(null)
    private var data by mutableStateOf<Data?>(null)

    private val coroutineScope = MainScope()
    private var loadJob: Job? = null

    private val getTasks: GetTasksUseCase by inject()

    data class Data(val pending: Map<String, List<Task>>)

    private suspend fun loadData(): Data {
        LocalGlanceId
        val pending = getTasks.getPendingTasks(1L).first()
            .groupBy { it.dueDate }
        println(pending)
        return Data(pending = pending)
    }

    fun initiateLoad() {
        loadJob?.cancel()
        loadJob = coroutineScope.launch {
            data = loadData()
            val ids = manager.getGlanceIds(PendingTasksWidget::class.java)
            println("Glance ID is: $glanceId")
            val currentGlanceId = snapshotFlow { glanceId }.filterNotNull().firstOrNull()
            currentGlanceId?.run {
                println("Updating Widget of ID: $this")
                update(context, this)
            }
        }
    }

    @OptIn(ExperimentalUnitApi::class)
    @Composable
    override fun Content() {
        glanceId = LocalGlanceId.current

        LazyColumn(
            modifier = GlanceModifier.background(Color.DarkGray).padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            if (data == null) {
                item {
                    Text(
                        modifier = GlanceModifier.padding(8.dp),
                        text = "No Tasks",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = TextUnit(20f, TextUnitType.Sp),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                data!!.pending.forEach { tasks ->
                    item {
                        Text(
                            modifier = GlanceModifier.padding(vertical = 4.dp),
                            text = tasks.key,
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontSize = TextUnit(16f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    items(tasks.value) { item ->
                        Text(
                            modifier = GlanceModifier.padding(bottom = 4.dp),
                            text = item.title,
                            style = TextStyle(
                                color = ColorProvider(Color.White),
                                fontSize = TextUnit(14f, TextUnitType.Sp),
                            )
                        )
                    }
                }
            }
        }
    }
}