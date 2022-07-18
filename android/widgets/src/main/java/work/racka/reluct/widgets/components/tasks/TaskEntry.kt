package work.racka.reluct.widgets.components.tasks

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.Action
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.CheckBoxColors
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.widgets.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.widgets.core.GlanceTheme

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
            .background(if (task.overdue) GlanceTheme.colors.error else GlanceTheme.colors.surfaceVariant)
                then modifier
    ) {
        Row(
            modifier = GlanceModifier
                .padding(Dimens.SmallPadding.size)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start
        ) {
            CheckBox(
                checked = task.done,
                onCheckedChange = onCheckedChange,
                colors = CheckBoxColors(
                    if (task.overdue) (R.color.colorOnError) else (R.color.colorOnSurface)
                )
            )
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
            TaskHeading(
                modifier = GlanceModifier.defaultWeight(),
                text = task.title,
                isOverdue = task.overdue
            )
            Spacer(modifier = GlanceModifier.width(Dimens.ExtraSmallPadding.size))
            Text(
                modifier = modifier,
                text = task.dueTime,
                style = TextStyle(
                    color = if (task.overdue) GlanceTheme.colors.onError
                    else GlanceTheme.colors.onSurface,
                    fontSize = 12.sp,
                ),
                maxLines = 1
            )
        }
        Spacer(modifier = GlanceModifier.height(Dimens.ExtraSmallPadding.size))
        TaskDescription(text = task.description, isOverdue = task.overdue)
        Spacer(modifier = GlanceModifier.height(Dimens.ExtraSmallPadding.size))
        TaskTimeInfo(
            timeText = task.timeLeftLabel,
            showOverdueLabel = showOverDueLabel,
            overdue = task.overdue
        )
    }
}