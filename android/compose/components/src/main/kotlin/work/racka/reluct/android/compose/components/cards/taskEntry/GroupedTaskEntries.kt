package work.racka.reluct.android.compose.components.cards.taskEntry

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import work.racka.reluct.android.compose.components.datetime.core.MaterialDialog
import work.racka.reluct.android.compose.components.datetime.core.rememberMaterialDialogState
import work.racka.reluct.android.compose.components.util.PreviewData
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.compose.common.date.time.picker.date.DatePicker

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

@Preview
@Composable
internal fun GroupedTasksPrev() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        val dialogState = rememberMaterialDialogState()
        val list = listOf(
            PreviewData.task1,
            PreviewData.task2,
            PreviewData.task3,
            PreviewData.task4,
            PreviewData.task5
        ).toImmutableList()
        val context = LocalContext.current
        Column {
            GroupedTaskEntries(
                entryType = EntryType.PendingTask,
                groupTitle = "Monday",
                taskList = list,
                onEntryClicked = { dialogState.show() },
                onCheckedChange = { _, _ -> }
            )
        }

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                PositiveButton(text = "Ok")
                NegativeButton(text = "Cancel")
            },
            content = {
                DatePicker { dateTime ->
                    Toast.makeText(context, dateTime.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }
}
