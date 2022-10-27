package work.racka.reluct.android.compose.components.bottom_sheet.add_edit_goal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.datetime.LocalDateTime
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task.DateTimePills
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.date.SelectedDaysOfWeekViewer
import work.racka.reluct.android.compose.components.number_picker.HoursNumberPicker
import work.racka.reluct.android.compose.components.number_picker.NumberPicker
import work.racka.reluct.android.compose.components.number_picker.convertMillisToTime
import work.racka.reluct.android.compose.components.number_picker.convertTimeToMillis
import work.racka.reluct.android.compose.components.textfields.ReluctTextField
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.app_info.AppInfo
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.time.TimeUtils.plus
import work.racka.reluct.common.model.util.time.Week

@Composable
internal fun GoalTypeSelector(
    modifier: Modifier = Modifier,
    selectedGoalType: GoalType,
    onSelectGoalType: (GoalType) -> Unit
) {
    @Composable
    fun getGoalTypeString(goalType: GoalType): String =
        when (goalType) {
            GoalType.DeviceScreenTimeGoal -> stringResource(R.string.device_screen_time_goal_type)
            GoalType.AppScreenTimeGoal -> stringResource(R.string.app_screen_time_goal_type)
            GoalType.TasksGoal -> stringResource(R.string.tasks_goal_type)
            GoalType.NumeralGoal -> stringResource(R.string.numeral_goal_type)
        }

    val rowState = rememberLazyListState()
    var selectedItemIndex by remember { mutableStateOf(0) }

    LaunchedEffect(selectedItemIndex) {
        if (selectedItemIndex != 0) {
            rowState.animateScrollToItem(selectedItemIndex)
        }
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = rowState,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        itemsIndexed(GoalType.values()) { index, type ->
            selectedItemIndex = if (selectedGoalType == type) index else selectedItemIndex

            ReluctButton(
                buttonText = getGoalTypeString(goalType = type),
                icon = null,
                shape = Shapes.large,
                buttonColor = if (selectedGoalType == type) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (selectedGoalType == type) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                onButtonClicked = { onSelectGoalType(type) }
            )
        }
    }
}

@Composable
internal fun GoalIntervalSelector(
    modifier: Modifier = Modifier,
    selectedGoalInterval: GoalInterval,
    onSelectGoalInterval: (GoalInterval) -> Unit
) {
    @Composable
    fun getGoalTypeString(goalInterval: GoalInterval): String =
        when (goalInterval) {
            GoalInterval.Daily -> stringResource(R.string.daily_interval_text)
            GoalInterval.Weekly -> stringResource(R.string.weekly_interval_text)
            GoalInterval.Custom -> stringResource(R.string.custom_interval_text)
        }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        GoalInterval.values().forEach { item ->
            ReluctButton(
                modifier = Modifier.weight(1f),
                buttonText = getGoalTypeString(goalInterval = item),
                icon = null,
                shape = Shapes.large,
                buttonColor = if (selectedGoalInterval == item) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (selectedGoalInterval == item) MaterialTheme.colorScheme.onPrimary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                onButtonClicked = { onSelectGoalInterval(item) }
            )
        }
    }
}

@Composable
internal fun AddEditGoalItemTitle(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = LocalContentColor.current,
    maxLines: Int = 1
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectedAppsCard(
    modifier: Modifier = Modifier,
    apps: List<AppInfo>,
    onEditApps: () -> Unit,
    appIconSize: Dp = 36.dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onEditApps
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier
                    .height(appIconSize)
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
            ) {
                if (apps.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(R.string.no_apps_text),
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalContentColor.current,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    items(apps) { app ->
                        Image(
                            modifier = Modifier.size(appIconSize),
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(app.appIcon.icon).build()
                            ),
                            contentDescription = app.appName
                        )
                    }
                }
            }

            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
        }
    }
}

@Composable
fun GoalScreenTimePicker(
    modifier: Modifier = Modifier,
    currentTimeMillis: Long,
    onUpdateCurrentTime: (timeMillis: Long) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val pickerValue by remember(currentTimeMillis) {
        derivedStateOf {
            convertMillisToTime(currentTimeMillis)
        }
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        HoursNumberPicker(
            modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
            value = pickerValue,
            onValueChange = { onUpdateCurrentTime(convertTimeToMillis(it)) },
            dividersColor = contentColor,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                color = contentColor
            ),
            hoursDivider = {
                Text(
                    text = "hr",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(color = contentColor)
                )
            },
            minutesDivider = {
                Text(
                    text = "m",
                    style = MaterialTheme.typography.bodyLarge
                        .copy(color = contentColor)
                )
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
internal fun LazyListScope.goalTargetValuePicker(
    keyboardController: SoftwareKeyboardController?,
    goalType: GoalType,
    targetValue: Long,
    onUpdateTargetValue: (Long) -> Unit
) {
    if (goalType == GoalType.DeviceScreenTimeGoal || goalType == GoalType.AppScreenTimeGoal) {
        item {
            AddEditGoalItemTitle(
                modifier = Modifier.animateItemPlacement(),
                text = stringResource(R.string.app_time_limit),
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            GoalScreenTimePicker(
                modifier = Modifier.animateItemPlacement(),
                currentTimeMillis = targetValue,
                onUpdateCurrentTime = onUpdateTargetValue
            )
        }
    } else {
        item {
            var textValue by remember { mutableStateOf(targetValue.toString()) }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AddEditGoalItemTitle(
                    modifier = Modifier
                        .weight(1f)
                        .animateItemPlacement(),
                    text = if (goalType == GoalType.TasksGoal) stringResource(R.string.select_number_of_tasks_txt)
                    else stringResource(R.string.target_value_txt),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))

                ReluctTextField(
                    value = textValue,
                    isError = textValue.isBlank(),
                    errorText = stringResource(id = R.string.invalid_value_text),
                    singleLine = true,
                    hint = if (goalType == GoalType.TasksGoal) stringResource(R.string.enter_number_of_tasks_txt)
                    else stringResource(R.string.enter_target_value),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    onTextChange = { text ->
                        textValue = text
                        if (text.isNotBlank()) {
                            text.toLongOrNull()?.run(onUpdateTargetValue)
                                ?: run { textValue = "" }
                        }
                    },
                    textStyle = MaterialTheme.typography.titleMedium
                        .copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth(.6f)
                )
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.goalDurationPicker(
    dateTimeRange: ClosedRange<LocalDateTime>,
    currentDaysOfWeek: List<Week>,
    goalInterval: GoalInterval,
    onDateTimeRangeChange: (ClosedRange<LocalDateTime>) -> Unit,
    onUpdateDaysOfWeek: (List<Week>) -> Unit
) {
    when (goalInterval) {
        GoalInterval.Daily -> {
            item {
                AddEditGoalItemTitle(
                    modifier = Modifier
                        .animateItemPlacement(),
                    text = stringResource(R.string.choose_days_txt),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                SelectedDaysOfWeekViewer(
                    modifier = Modifier.animateItemPlacement(),
                    selectedDays = currentDaysOfWeek,
                    onUpdateDaysOfWeek = onUpdateDaysOfWeek
                )
            }
        }
        GoalInterval.Custom -> {
            item {
                AddEditGoalItemTitle(
                    modifier = Modifier
                        .animateItemPlacement(),
                    text = stringResource(R.string.start_time_txt),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                DateTimePills(
                    modifier = Modifier.animateItemPlacement(),
                    currentLocalDateTime = dateTimeRange.start,
                    onLocalDateTimeChange = { dateTime ->
                        val endTime = if (dateTime >= dateTimeRange.endInclusive)
                            dateTime.plus(hours = 1) else dateTimeRange.endInclusive
                        onDateTimeRangeChange(dateTime..endTime)
                    }
                )
            }

            item {
                var endTimeError by remember { mutableStateOf(false) }

                AddEditGoalItemTitle(
                    modifier = Modifier
                        .animateItemPlacement(),
                    text = stringResource(R.string.end_time_txt),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                DateTimePills(
                    modifier = Modifier.animateItemPlacement(),
                    hasError = endTimeError,
                    errorText = stringResource(id = R.string.goal_time_error_text),
                    currentLocalDateTime = dateTimeRange.endInclusive,
                    onLocalDateTimeChange = { dateTime ->
                        endTimeError = dateTime <= dateTimeRange.start
                        val endTime =
                            if (dateTime <= dateTimeRange.start) dateTimeRange.endInclusive
                            else dateTime
                        onDateTimeRangeChange(dateTimeRange.start..endTime)
                    }
                )
            }
        }
        else -> {}
    }
}

@Composable
private fun GoalNumberPicker(
    modifier: Modifier = Modifier,
    name: String,
    currentValue: Long,
    onUpdateCurrentValue: (Long) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    numberRange: IntRange = 0..10000
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        AddEditGoalItemTitle(
            modifier = Modifier.padding(Dimens.MediumPadding.size),
            text = name
        )
        NumberPicker(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxWidth(),
            value = currentValue.toInt(),
            onValueChange = { onUpdateCurrentValue(it.toLong()) },
            range = numberRange,
            textStyle = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReluctSelectionButton(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    isSelected: Boolean,
    content: @Composable ColumnScope.() -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        content = content
    )
}