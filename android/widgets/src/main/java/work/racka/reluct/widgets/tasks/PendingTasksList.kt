package work.racka.reluct.widgets.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.widgets.components.tasks.WidgetTaskEntry
import work.racka.reluct.widgets.core.WidgetTheme
import work.racka.reluct.widgets.tasks.actions.OpenTaskDetailsAction
import work.racka.reluct.widgets.tasks.actions.PendingTasksWidgetParamKeys
import work.racka.reluct.widgets.tasks.actions.ToggleTaskDoneAction
import work.racka.reluct.widgets.tasks.actions.asWidgetTaskParcel

@Composable
fun PendingTasksList(pendingTasks: Map<String, List<Task>>?) {
    LazyColumn(
        modifier = GlanceModifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        // Top Space
        item { Spacer(modifier = GlanceModifier.height(Dimens.SmallPadding.size)) }

        if (pendingTasks == null) {
            item {
                Text(
                    modifier = GlanceModifier
                        .padding(16.dp),
                    text = "Loading...",
                    style = TextStyle(
                        color = WidgetTheme.Colors.onBackground,
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
                        modifier = GlanceModifier.padding(
                            vertical = 4.dp,
                            horizontal = 8.dp
                        ),
                        text = tasks.key,
                        style = TextStyle(
                            color = WidgetTheme.Colors.onBackground,
                            fontSize = 16.sp,
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