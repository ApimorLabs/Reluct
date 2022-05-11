package work.racka.reluct.android.compose.components.cards.task_entry

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.compose.theme.Typography
import work.racka.reluct.common.model.domain.tasks.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfoCard(
    modifier: Modifier = Modifier,
    task: Task,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
) {

    Card(
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .clip(shape)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(Dimens.SmallPadding.size)
        ) {
            TaskInfoEntry(
                text = stringResource(R.string.task_info_due_text, task.dueDate, task.dueTime),
                icon = Icons.Rounded.Schedule,
                contentDescription = null
            )

            TaskInfoEntry(
                text = stringResource(R.string.task_info_reminder_text, task.reminder),
                icon = Icons.Rounded.NotificationsActive,
                contentDescription = null
            )

            TaskInfoEntry(
                text = if (task.overdue) stringResource(id = R.string.overdue_text)
                else stringResource(id = R.string.in_time_text),
                icon = Icons.Rounded.Timer,
                contentDescription = null,
                color = if (task.overdue) MaterialTheme.colorScheme.error
                else Color.Green
            )

            if (task.done) {
                TaskInfoEntry(
                    text = stringResource(R.string.task_completed_date_time_text,
                        task.completedDateAndTime),
                    icon = Icons.Rounded.AlarmOn,
                    contentDescription = null
                )
            }

            if (!task.done) {
                TaskInfoEntry(
                    text = task.timeLeftLabel,
                    icon = Icons.Rounded.Timelapse,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
internal fun TaskInfoEntry(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    color: Color = LocalContentColor.current,
    icon: ImageVector,
    contentDescription: String?,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(Dimens.SmallPadding.size),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
        TaskInfoText(
            modifier = modifier.fillMaxWidth(),
            text = text,
            style = textStyle,
            color = color
        )
    }
}

@Composable
internal fun TaskInfoText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle,
    color: Color = LocalContentColor.current,
) {
    Typography
    Text(
        modifier = modifier,
        text = text,
        style = style,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = color
    )
}