package work.racka.reluct.widgets.tasks

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalGlanceId
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.data.usecases.tasks.GetTasksUseCase
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.widgets.components.tasks.WidgetTaskEntry
import work.racka.reluct.widgets.core.WidgetTheme
import work.racka.reluct.widgets.tasks.actions.OpenTaskDetailsAction
import work.racka.reluct.widgets.tasks.actions.PendingTasksWidgetParamKeys
import work.racka.reluct.widgets.tasks.actions.ToggleTaskDoneAction
import work.racka.reluct.widgets.tasks.actions.asWidgetTaskParcel

class PendingTasksWidget : GlanceAppWidget(), KoinComponent {

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    private val context: Context by inject()

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
            println("Glance ID is: $glanceId")
            val currentGlanceId = snapshotFlow { glanceId }.filterNotNull().firstOrNull()
            glanceId?.run {
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
            modifier = GlanceModifier.background(WidgetTheme.Colors.background)
                .cornerRadius(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            // Top Space
            item { Spacer(modifier = GlanceModifier.height(Dimens.SmallPadding.size)) }

            if (data == null) {
                item {
                    Text(
                        modifier = GlanceModifier
                            .padding(16.dp),
                        text = "Loading...",
                        style = TextStyle(
                            color = WidgetTheme.Colors.onBackground,
                            fontSize = TextUnit(20f, TextUnitType.Sp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            } else {
                data!!.pending.forEach { tasks ->
                    item {
                        Text(
                            modifier = GlanceModifier.padding(vertical = 4.dp, horizontal = 8.dp),
                            text = tasks.key,
                            style = TextStyle(
                                color = WidgetTheme.Colors.onBackground,
                                fontSize = TextUnit(16f, TextUnitType.Sp),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    items(tasks.value) { item ->
                        Column(
                            modifier = GlanceModifier.padding(horizontal = 8.dp)
                        ) {
                            WidgetTaskEntry(
                                task = item,
                                onCheckedChange = actionRunCallback<ToggleTaskDoneAction>(
                                    parameters = actionParametersOf(
                                        PendingTasksWidgetParamKeys.TASK_KEY to item.asWidgetTaskParcel()
                                    )
                                ),
                                onEntryClick = actionRunCallback<OpenTaskDetailsAction>()
                            )
                            Spacer(modifier = GlanceModifier.height(Dimens.SmallPadding.size))
                        }
                    }
                }
            }

            // Bottom Space
            item { Spacer(modifier = GlanceModifier.height(Dimens.SmallPadding.size)) }
        }
    }
}