package work.racka.reluct.widgets.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import work.racka.reluct.widgets.core.GlanceTheme
import work.racka.reluct.widgets.tasks.actions.ReloadTasksAction
import work.racka.reluct.widgets.tasks.state.PendingTasksInfo
import work.racka.reluct.widgets.tasks.state.PendingTasksStateDefinition

class PendingTasksWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PendingTasksStateDefinition

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    @Composable
    override fun Content() {
        // Get the stored stated based on our custom state definition.
        val tasksInfo = currentState<PendingTasksInfo>()
        GlanceTheme {
            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .appWidgetBackground()
                    .background(GlanceTheme.colors.background)
                    .cornerRadius(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = GlanceModifier.fillMaxSize(),
                    verticalAlignment = Alignment.Vertical.Top,
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Box(
                        modifier = GlanceModifier.height(48.dp)
                            .fillMaxWidth()
                            .background(GlanceTheme.colors.primary),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            modifier = GlanceModifier
                                .padding(horizontal = 16.dp),
                            text = "Tasks",
                            style = TextStyle(
                                color = GlanceTheme.colors.onPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    when (tasksInfo) {
                        is PendingTasksInfo.Loading -> {
                            CircularProgressIndicator(
                                GlanceModifier.clickable(
                                    actionRunCallback<ReloadTasksAction>()
                                )
                            )
                        }
                        is PendingTasksInfo.Data -> {
                            PendingTasksList(pendingTasks = tasksInfo.pendingTasks)
                        }
                        is PendingTasksInfo.Nothing -> {
                            Text(
                                modifier = GlanceModifier
                                    .padding(16.dp),
                                text = "No Tasks",
                                style = TextStyle(
                                    color = GlanceTheme.colors.onBackground,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}