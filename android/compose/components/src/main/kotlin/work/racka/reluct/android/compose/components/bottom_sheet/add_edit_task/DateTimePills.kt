package work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
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
fun DateTimePills(
    modifier: Modifier = Modifier,
    initialLocalDateTime: LocalDateTime,
    onLocalDateTimeChange: (dateTimeString: String) -> Unit,
) {

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    val localDateTime = remember {
        mutableStateOf(
            initialLocalDateTime
        )
    }

    val localDateTimeString = rememberSaveable(localDateTime.value) {
        mutableStateOf(
            localDateTime.value.toString()
        )
    }

    val dateString = rememberSaveable(localDateTime.value) {
        mutableStateOf(
            TimeUtils.getFormattedDateString(
                dateTime = localDateTime.value.toString(),
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
        )
    }

    val timeString = rememberSaveable(localDateTime.value) {
        mutableStateOf(
            TimeUtils.getTimeFromLocalDateTime(
                dateTime = localDateTime.value.toString(),
                originalTimeZoneId = TimeZone.currentSystemDefault().id
            )
        )
    }

    DateAndTimeMaterialDialogs(
        initialLocalDateTime = localDateTime.value,
        dateDialogState = dateDialogState,
        timeDialogState = timeDialogState,
        onLocalDateTimeChange = { dateChange, timeChange ->
            dateChange?.let { localDateTime.value = it }
            timeChange?.let { localDateTime.value = it }
            onLocalDateTimeChange(
                localDateTimeString.value
            )
        },
    )

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
            buttonColor = MaterialTheme.colorScheme.surfaceVariant
        )

        // Time
        ReluctButton(
            modifier = Modifier
                .weight(1f),
            buttonText = timeString.value,
            icon = Icons.Rounded.Schedule,
            onButtonClicked = { timeDialogState.show() },
            shape = Shapes.large,
            buttonColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
private fun DateAndTimeMaterialDialogs(
    initialLocalDateTime: LocalDateTime,
    dateDialogState: MaterialDialogState,
    timeDialogState: MaterialDialogState,
    onLocalDateTimeChange: (dateChange: LocalDateTime?, timeChange: LocalDateTime?) -> Unit,
) {

    val localDateTime = remember {
        mutableStateOf(
            initialLocalDateTime
        )
    }

    // Date
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            PositiveButton(text = stringResource(id = R.string.positive_dialog_button))
            NegativeButton(text = stringResource(id = R.string.negative_dialog_button))
        }
    ) {
        DatePicker(
            initialDate = LocalDate(
                localDateTime.value.year,
                localDateTime.value.monthNumber,
                localDateTime.value.dayOfMonth
            )
        ) { dateTime ->
            localDateTime.value = LocalDateTime(
                dateTime.year,
                dateTime.monthNumber,
                dateTime.dayOfMonth,
                localDateTime.value.hour,
                localDateTime.value.minute,
                localDateTime.value.second,
                localDateTime.value.nanosecond
            )
            onLocalDateTimeChange(localDateTime.value, null)
        }
    }

    // Time
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            PositiveButton(text = stringResource(id = R.string.positive_dialog_button))
            NegativeButton(text = stringResource(id = R.string.negative_dialog_button))
        }
    ) {
        Timepicker(
            initialTime = localDateTime.value
        ) { dateTime ->
            localDateTime.value = LocalDateTime(
                localDateTime.value.year,
                localDateTime.value.monthNumber,
                localDateTime.value.dayOfMonth,
                dateTime.hour,
                dateTime.minute,
                dateTime.second,
                dateTime.nanosecond
            )
            onLocalDateTimeChange(null, localDateTime.value)
        }
    }
}