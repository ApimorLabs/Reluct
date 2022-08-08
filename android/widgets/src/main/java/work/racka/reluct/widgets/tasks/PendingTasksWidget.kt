package work.racka.reluct.widgets.tasks

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.widgets.R
import work.racka.reluct.common.core_navigation.compose_destinations.tasks.PendingTasksDestination
import work.racka.reluct.widgets.components.common.WidgetIconButton
import work.racka.reluct.widgets.components.common.WidgetTopBar
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
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .clickable(
                            onClick = actionStartActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    PendingTasksDestination.pendingTasksDeeplink().toUri()
                                )
                            )
                        ),
                    verticalAlignment = Alignment.Vertical.Top,
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    WidgetTopBar(
                        title = "Tasks",
                        actionButton = {
                            WidgetIconButton(onClick = actionRunCallback<ReloadTasksAction>()) {
                                Image(
                                    provider = ImageProvider(R.drawable.ic_round_refresh_24),
                                    contentDescription = "Refresh"
                                )
                            }
                        }
                    )

                    when (tasksInfo) {
                        is PendingTasksInfo.Loading -> {
                            CircularProgressIndicator(
                                GlanceModifier
                                    .padding(Dimens.MediumPadding.size)
                                    .clickable(
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