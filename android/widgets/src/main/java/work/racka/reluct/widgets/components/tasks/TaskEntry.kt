package work.racka.reluct.widgets.components.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.widgets.core.WidgetTheme

@Composable
fun WidgetTaskEntry(
    modifier: GlanceModifier = GlanceModifier,
    task: Task,
    onCheckedChange: Action,
    onEntryClick: Action
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = GlanceModifier
            .fillMaxWidth()
            .cornerRadius(15.dp)
            .clickable(onEntryClick)
            .background(if (task.overdue) ColorProvider(Color.Red) else WidgetTheme.Colors.surface)
                then modifier
    ) {
        Row(
            modifier = GlanceModifier
                .padding(Dimens.SmallPadding.size)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            CheckBox(checked = task.done, onCheckedChange = onCheckedChange)
            Spacer(modifier = GlanceModifier.width(Dimens.ExtraSmallPadding.size))
            WidgetTaskEntryText(task = task)
        }
    }
}

@Composable
private fun WidgetTaskEntryText(
    modifier: GlanceModifier = GlanceModifier,
    task: Task,
    showOverDueLabel: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            TaskHeading(modifier = GlanceModifier.defaultWeight(), text = task.title)
            Spacer(modifier = GlanceModifier.width(Dimens.ExtraSmallPadding.size))
            Text(
                modifier = modifier,
                text = task.dueTime,
                style = TextStyle(
                    color = androidx.glance.appwidget.unit.ColorProvider(
                        day = Color.Black.copy(alpha = .8f),
                        night = Color.White.copy(alpha = .8f)
                    ),
                    fontSize = 12.sp,
                ),
                maxLines = 1
            )
        }
        Spacer(modifier = GlanceModifier.height(Dimens.ExtraSmallPadding.size))
        TaskDescription(text = task.description)
        Spacer(modifier = GlanceModifier.height(Dimens.ExtraSmallPadding.size))
        TaskTimeInfo(
            timeText = task.timeLeftLabel,
            showOverdueLabel = showOverDueLabel,
            overdue = task.overdue
        )
    }
}