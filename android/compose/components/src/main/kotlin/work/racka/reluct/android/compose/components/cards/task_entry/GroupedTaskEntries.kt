package work.racka.reluct.android.compose.components.cards.task_entry

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import work.racka.reluct.android.compose.components.datetime.core.MaterialDialog
import work.racka.reluct.android.compose.components.datetime.core.rememberMaterialDialogState
import work.racka.reluct.android.compose.components.datetime.date.DatePicker
import work.racka.reluct.android.compose.components.textfields.ReluctTextField
import work.racka.reluct.android.compose.components.util.PreviewData
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.model.domain.tasks.Task

@Composable
fun GroupedTaskEntries(
    modifier: Modifier = Modifier,
    entryType: EntryType,
    groupTitle: String,
    taskList: List<Task>,
    onEntryClicked: (taskId: Long) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
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
                task = task,
                entryType = entryType,
                onEntryClick = { onEntryClicked(task.id) },
                onCheckedChange = { isChecked -> onCheckedChange(isChecked) }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
internal fun GroupedTasksPrev() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        val dialogState = rememberMaterialDialogState()
        val list = listOf(PreviewData.task1, PreviewData.task2,
            PreviewData.task3, PreviewData.task4, PreviewData.task5)
        Column {
            ReluctTextField(
                hint = "Task",
                modifier = Modifier
                    .fillMaxWidth(.9f)
            )
            GroupedTaskEntries(
                entryType = EntryType.PendingTask,
                groupTitle = "Monday",
                taskList = list,
                onEntryClicked = { dialogState.show() },
                onCheckedChange = {}
            )
        }

        val context = LocalContext.current

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                PositiveButton(text = "Ok")
                NegativeButton(text = "Cancel")
            }
        ) {
            DatePicker { dateTime ->
                Toast.makeText(context, dateTime.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}