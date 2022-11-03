package work.racka.reluct.android.compose.components.bottomSheet.addEditTask

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.datetime.core.MaterialDialog
import work.racka.reluct.android.compose.components.datetime.core.MaterialDialogState
import work.racka.reluct.android.compose.components.datetime.core.rememberMaterialDialogState
import work.racka.reluct.android.compose.components.datetime.date.DatePicker
import work.racka.reluct.android.compose.components.datetime.time.Timepicker
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.util.time.TimeUtils

@Composable
internal fun DateTimePills(
    currentLocalDateTime: LocalDateTime,
    onLocalDateTimeChange: (dateTime: LocalDateTime) -> Unit,
    modifier: Modifier = Modifier,
    dialogShape: Shape = Shapes.large,
    hasError: Boolean = false,
    errorText: String = "",
) {
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val pillContainerColor by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )
    val pillContentColor by animateColorAsState(
        targetValue = if (hasError) {
            MaterialTheme.colorScheme.onError
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )

    val dateString = rememberSaveable(currentLocalDateTime) {
        mutableStateOf(
            TimeUtils.getFormattedDateString(
                dateTime = currentLocalDateTime.toString(),
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
        )
    }

    val timeString = rememberSaveable(currentLocalDateTime) {
        mutableStateOf(
            TimeUtils.getTimeFromLocalDateTime(
                dateTime = currentLocalDateTime.toString(),
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
        )
    }

    DateAndTimeMaterialDialogs(
        shape = dialogShape,
        initialLocalDateTime = currentLocalDateTime,
        dateDialogState = dateDialogState,
        timeDialogState = timeDialogState,
        onLocalDateTimeChange = { dateChange, timeChange ->
            var dateTime = currentLocalDateTime
            dateChange?.let { dateTime = it }
            timeChange?.let { dateTime = it }
            onLocalDateTimeChange(dateTime)
        },
    )

    Column(verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
            modifier = modifier
                .fillMaxWidth()
        ) {
            // Date
            ReluctButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = dateString.value,
                icon = Icons.Rounded.DateRange,
                onButtonClicked = { dateDialogState.show() },
                shape = Shapes.large,
                contentColor = pillContentColor,
                buttonColor = pillContainerColor
            )

            // Time
            ReluctButton(
                modifier = Modifier
                    .weight(1f),
                buttonText = timeString.value,
                icon = Icons.Rounded.Schedule,
                onButtonClicked = { timeDialogState.show() },
                shape = Shapes.large,
                contentColor = pillContentColor,
                buttonColor = pillContainerColor
            )
        }

        if (hasError && errorText.isNotBlank()) {
            Text(text = errorText, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun DateAndTimeMaterialDialogs(
    initialLocalDateTime: LocalDateTime,
    dateDialogState: MaterialDialogState,
    timeDialogState: MaterialDialogState,
    onLocalDateTimeChange: (dateChange: LocalDateTime?, timeChange: LocalDateTime?) -> Unit,
    shape: Shape = Shapes.large,
) {
    // Date
    MaterialDialog(
        dialogState = dateDialogState,
        shape = shape,
        buttons = {
            PositiveButton(text = stringResource(id = R.string.positive_dialog_button))
            NegativeButton(text = stringResource(id = R.string.negative_dialog_button))
        },
        content = {
            DatePicker(
                initialDate = LocalDate(
                    initialLocalDateTime.year,
                    initialLocalDateTime.monthNumber,
                    initialLocalDateTime.dayOfMonth
                )
            ) { date ->
                val dateTime = LocalDateTime(
                    date.year,
                    date.monthNumber,
                    date.dayOfMonth,
                    initialLocalDateTime.hour,
                    initialLocalDateTime.minute,
                    initialLocalDateTime.second,
                    initialLocalDateTime.nanosecond
                )
                onLocalDateTimeChange(dateTime, null)
            }
        }
    )

    // Time
    MaterialDialog(
        dialogState = timeDialogState,
        shape = shape,
        buttons = {
            PositiveButton(text = stringResource(id = R.string.positive_dialog_button))
            NegativeButton(text = stringResource(id = R.string.negative_dialog_button))
        },
        content = {
            Timepicker(
                initialTime = initialLocalDateTime
            ) { dateTime ->
                val newDateTime = LocalDateTime(
                    initialLocalDateTime.year,
                    initialLocalDateTime.monthNumber,
                    initialLocalDateTime.dayOfMonth,
                    dateTime.hour,
                    dateTime.minute,
                    dateTime.second,
                    dateTime.nanosecond
                )
                onLocalDateTimeChange(null, newDateTime)
            }
        }
    )
}
