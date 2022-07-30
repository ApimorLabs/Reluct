package work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.settings.EntryWithCheckbox
import work.racka.reluct.android.compose.components.textfields.ReluctTextField
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.tasks.EditTask
import work.racka.reluct.common.model.util.time.TimeUtils.getLocalDateTimeWithCorrectTimeZone
import work.racka.reluct.common.model.util.time.TimeUtils.plus
import java.util.*

// This provided here so that it doesn't leak DateTime dependencies to the
// screens modules.
@Composable
fun LazyColumnAddEditTaskFields(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    editTask: EditTask?,
    saveButtonText: String,
    discardButtonText: String,
    onReminderSet: (reminderLocalDateTime: String) -> Unit = { },
    onSave: (EditTask) -> Unit,
    onDiscard: () -> Unit = { },
) {
    val setReminder = rememberSaveable(editTask) {
        val reminderPresent = !editTask?.reminderLocalDateTime.isNullOrBlank()
        mutableStateOf(reminderPresent)
    }

    val taskTitleError = remember {
        mutableStateOf(false)
    }

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
                reminderLocalDateTime = null
            )
        mutableStateOf(taskToEdit)
    }

    val initialDueTime = remember {
        derivedStateOf {
            getLocalDateTimeWithCorrectTimeZone(
                dateTime = task.value.dueDateLocalDateTime,
                originalTimeZoneId = task.value.timeZoneId
            )
        }
    }

    val initialReminderTime = remember {
        derivedStateOf {
            if (!task.value.reminderLocalDateTime.isNullOrBlank()) {
                getLocalDateTimeWithCorrectTimeZone(
                    dateTime = task.value.reminderLocalDateTime!!,
                    originalTimeZoneId = task.value.timeZoneId
                )
            } else advancedDateTime.value
        }
    }

    LazyColumn(
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement
            .spacedBy(Dimens.MediumPadding.size),
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
    ) {
        item {
            ReluctTextField(
                value = task.value.title,
                hint = stringResource(R.string.task_title_hint),
                isError = taskTitleError.value,
                errorText = stringResource(R.string.task_title_error_text),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                onTextChange = { text ->
                    taskTitleError.value = false
                    task.value = task.value.copy(title = text)
                }
            )
        }

        item {
            ReluctTextField(
                modifier = Modifier
                    .height(200.dp),
                value = task.value.description ?: "",
                hint = stringResource(R.string.task_description_hint),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                onTextChange = { text ->
                    task.value = task.value.copy(description = text)
                }
            )
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.task_to_be_done_at_text),
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            DateTimePills(
                initialLocalDateTime = initialDueTime.value,
                onLocalDateTimeChange = { dateTimeString ->
                    task.value = task.value.copy(dueDateLocalDateTime = dateTimeString)
                }
            )
        }

        item {
            EntryWithCheckbox(
                isChecked = setReminder.value,
                title = stringResource(R.string.set_reminder),
                description = stringResource(R.string.set_reminder_desc),
                onCheckedChanged = { checked ->
                    setReminder.value = checked
                    task.value = task.value.copy(
                        reminderLocalDateTime =
                        if (setReminder.value) advancedDateTime.value.toString()
                        else null
                    )
                }
            )
        }

        item {
            AnimatedVisibility(
                visible = setReminder.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.reminder_at),
                        style = MaterialTheme.typography.titleMedium,
                        color = LocalContentColor.current,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                    DateTimePills(
                        initialLocalDateTime = initialReminderTime.value,
                        onLocalDateTimeChange = { dateTimeString ->
                            task.value = task.value.copy(reminderLocalDateTime = dateTimeString)
                        }
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReluctButton(
                    buttonText = discardButtonText,
                    icon = Icons.Rounded.DeleteSweep,
                    onButtonClicked = onDiscard,
                    shape = Shapes.large,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    buttonColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
                ReluctButton(
                    buttonText = saveButtonText,
                    icon = Icons.Rounded.Save,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        val isTitleBlank = task.value.title.isBlank()
                        if (isTitleBlank) taskTitleError.value = true
                        else onSave(task.value)

                        task.value.reminderLocalDateTime?.let { onReminderSet(it) }
                    }
                )
            }
        }
    }
}
