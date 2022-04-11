package work.racka.reluct.android.compose.components.cards.task_entry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import work.racka.reluct.android.compose.components.util.PreviewData
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.tasks.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TaskEntry(
    modifier: Modifier = Modifier,
    task: Task,
    entryType: EntryType,
    onEntryClick: () -> Unit = { },
    onCheckedChange: (Boolean) -> Unit = { },
) {

    Card(
        containerColor = if (entryType == EntryType.PendingTaskOverdue && task.overdue)
            MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.surfaceVariant,
        onClick = { onEntryClick() },
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            RoundCheckbox(
                isChecked = task.done,
                onCheckedChange = {
                    onCheckedChange(it)
                }
            )
            Spacer(modifier = Modifier.width(Dimens.SmallPadding.size))
            TaskEntryText(
                task = task,
                entryType = entryType
            )
        }
    }

}

@Composable
private fun TaskEntryText(
    modifier: Modifier = Modifier,
    task: Task,
    entryType: EntryType,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TaskHeading(text = task.title)
            Spacer(modifier = Modifier.width(Dimens.SmallPadding.size))
            Text(
                text = task.dueTime,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = LocalContentColor.current.copy(alpha = .8f)
            )
        }
        AnimatedVisibility(visible = entryType == EntryType.PendingTask) {
            Spacer(modifier = Modifier.height(Dimens.ExtraSmallPadding.size))
            TaskDescription(text = task.description)
        }
        Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
        TaskTimeInfo(
            timeText = if (
                entryType == EntryType.PendingTask ||
                entryType == EntryType.PendingTaskOverdue
            ) task.timeLeftLabel
            else task.dueDate,
            showOverdueLabel = entryType == EntryType.CompletedTask,
            overdue = task.overdue
        )
    }
}

@Preview
@Composable
internal fun TaskEntryPrev() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            Modifier
                .scrollable(
                    rememberScrollState(),
                    orientation = Orientation.Vertical
                )
        ) {
            TaskEntry(
                task = PreviewData.task1,
                entryType = EntryType.PendingTask
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            TaskEntry(
                task = PreviewData.task2,
                entryType = EntryType.CompletedTask
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            TaskEntry(
                task = PreviewData.task5,
                entryType = EntryType.CompletedTask
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            TaskEntry(
                task = PreviewData.task3,
                entryType = EntryType.Statistics
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            TaskEntry(
                task = PreviewData.task4,
                entryType = EntryType.PendingTaskOverdue
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
        }
    }
}