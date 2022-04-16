package work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.bottom_sheet.TopSheetSection
import work.racka.reluct.android.compose.components.textfields.ReluctTextField
import work.racka.reluct.android.compose.theme.BottomSheetShape
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.util.time.TimeUtils.plus
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditTaskSheet(
    modifier: Modifier = Modifier,
    editTask: EditTask?,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = BottomSheetShape,
    onSave: (EditTask) -> Unit = { },
    onClose: () -> Unit = { },
) {
    val setReminder = remember {
        mutableStateOf(false)
    }

    val taskTitleError = remember {
        mutableStateOf(false)
    }

    val sheetTitle = if (editTask == null) stringResource(id = R.string.add_task)
    else stringResource(id = R.string.edit_task)

    val advancedDateTime = remember {
        derivedStateOf {
            Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .plus(hours = 1)
        }
    }

    val task = remember {
        val taskToEdit = editTask
            ?: EditTask(
                id = UUID.randomUUID().toString(),
                title = "",
                description = null,
                done = false,
                overdue = false,
                dueDateLocalDateTime = advancedDateTime.value.toString(),
                timeZoneId = TimeZone.currentSystemDefault().id,
                completedLocalDateTime = null,
                reminderLocalDateTime =
                if (setReminder.value) advancedDateTime.value.toString()
                else null
            )
        mutableStateOf(taskToEdit)
    }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding(),
        shape = shape,
        color = containerColor
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding(),
            verticalArrangement = Arrangement
                .spacedBy(Dimens.MediumPadding.size)
        ) {
            TopSheetSection(
                sheetTitle = sheetTitle,
                modifier = Modifier
                    .padding(top = Dimens.SmallPadding.size)
                    .padding(horizontal = Dimens.SmallPadding.size),
                onCloseClicked = onClose
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement
                    .spacedBy(Dimens.MediumPadding.size),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    ReluctTextField(
                        hint = stringResource(R.string.task_title_hint),
                        isError = taskTitleError.value,
                        errorText = stringResource(R.string.task_title_error_text),
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        onTextChange = { text ->
                            task.value = task.value.copy(title = text)
                        }
                    )
                }

                item {
                    ReluctTextField(
                        modifier = Modifier
                            .height(200.dp),
                        hint = stringResource(R.string.task_description_hint),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        onTextChange = { text ->
                            task.value = task.value.copy(title = text)
                        }
                    )
                }

                item {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = Dimens.MediumPadding.size)
                            .fillMaxWidth(),
                        text = stringResource(R.string.task_to_be_done_at_text),
                        style = MaterialTheme.typography.titleMedium,
                        color = LocalContentColor.current,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                    DateTimePills(
                        onLocalDateTimeChange = { dateTimeString ->
                            task.value = task.value.copy(dueDateLocalDateTime = dateTimeString)
                        }
                    )
                }
            }
        }
    }

}


@Preview
@Composable
internal fun AddEditTaskSheetPreview() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        AddEditTaskSheet(
            editTask = null,
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
        )
    }
}