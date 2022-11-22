package work.racka.reluct.compose.common.components.cards.taskEntry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.compose.common.theme.Dimens

@Composable
fun GroupedTaskEntries(
    entryType: EntryType,
    groupTitle: String,
    taskList: ImmutableList<Task>,
    onEntryClicked: (task: Task) -> Unit,
    onCheckedChange: (isDone: Boolean, task: Task) -> Unit,
    modifier: Modifier = Modifier,
    playScaleAnimation: Boolean = false,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        Text(
            modifier = Modifier
                .padding(start = Dimens.SmallPadding.size),
            text = groupTitle,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = LocalContentColor.current
        )

        taskList.forEach { task ->
            TaskEntry(
                playAnimation = playScaleAnimation,
                task = task,
                entryType = entryType,
                onEntryClick = { onEntryClicked(task) },
                onCheckedChange = { isChecked ->
                    onCheckedChange(isChecked, task)
                }
            )
        }
    }
}

/*
@Preview
@Composable
internal fun GroupedTasksPrev() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        val list = listOf(
            PreviewData.task1,
            PreviewData.task2,
            PreviewData.task3,
            PreviewData.task4,
            PreviewData.task5
        ).toImmutableList()
        Column {
            GroupedTaskEntries(
                entryType = EntryType.PendingTask,
                groupTitle = "Monday",
                taskList = list,
                onEntryClicked = { },
                onCheckedChange = { _, _ -> }
            )
        }
    }
}*/
