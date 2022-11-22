package work.racka.reluct.widgets.tasks

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import work.racka.reluct.common.core.navigation.composeDestinations.tasks.TaskDetailsDestination
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.widgets.components.tasks.WidgetTaskEntry
import work.racka.reluct.widgets.core.GlanceTheme
import work.racka.reluct.widgets.tasks.actions.PendingTasksWidgetParamKeys
import work.racka.reluct.widgets.tasks.actions.ToggleTaskDoneAction
import work.racka.reluct.widgets.tasks.state.WidgetTaskParcel
import work.racka.reluct.widgets.tasks.state.asTask

@Suppress("UnstableCollections")
@Composable
internal fun PendingTasksList(pendingTasks: Map<String, List<WidgetTaskParcel>>) {
    LazyColumn(
        modifier = GlanceModifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        // Top Space
        item { Spacer(modifier = GlanceModifier.height(Dimens.SmallPadding.size)) }

        if (pendingTasks.isEmpty()) {
            item {
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
        } else {
            pendingTasks.forEach { tasks ->
                item {
                    Text(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 4.dp,
                                horizontal = 8.dp
                            ),
                        text = tasks.key,
                        style = TextStyle(
                            color = GlanceTheme.colors.onBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    )
                }

                items(tasks.value) { item ->
                    Column(
                        modifier = GlanceModifier.padding(horizontal = 8.dp)
                    ) {
                        WidgetTaskEntry(
                            task = item.asTask(),
                            onCheckedChange = actionRunCallback<ToggleTaskDoneAction>(
                                parameters = actionParametersOf(
                                    PendingTasksWidgetParamKeys.TASK_KEY to item
                                )
                            ),
                            onEntryClick = actionStartActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    TaskDetailsDestination.taskDetailsDeepLink(item.id).toUri()
                                )
                            )
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
