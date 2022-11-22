package work.racka.reluct.compose.common.components.bottomSheet.addEditGoal

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
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDateTime
import work.racka.reluct.common.model.domain.appInfo.AppInfo
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.time.TimeUtils.plus
import work.racka.reluct.common.model.util.time.Week
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.bottomSheet.addEditTask.DateTimePills
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.cards.date.SelectedDaysOfWeekViewer
import work.racka.reluct.compose.common.components.images.painterResource
import work.racka.reluct.compose.common.components.numberPicker.HoursNumberPicker
import work.racka.reluct.compose.common.components.numberPicker.NumberPicker
import work.racka.reluct.compose.common.components.numberPicker.convertMillisToTime
import work.racka.reluct.compose.common.components.numberPicker.convertTimeToMillis
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.textfields.ReluctTextField
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@Composable
internal fun GoalTypeSelector(
    selectedGoalType: GoalType,
    onSelectGoalType: (GoalType) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun getGoalTypeString(goalType: GoalType): String =
        when (goalType) {
            GoalType.DeviceScreenTimeGoal -> stringResource(SharedRes.strings.device_screen_time_goal_type)
            GoalType.AppScreenTimeGoal -> stringResource(SharedRes.strings.app_screen_time_goal_type)
            GoalType.TasksGoal -> stringResource(SharedRes.strings.tasks_goal_type)
            GoalType.NumeralGoal -> stringResource(SharedRes.strings.numeral_goal_type)
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
                buttonColor = if (selectedGoalType == type) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (selectedGoalType == type) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                onButtonClicked = { onSelectGoalType(type) }
            )
        }
    }
}

@Composable
internal fun GoalIntervalSelector(
    selectedGoalInterval: GoalInterval,
    onSelectGoalInterval: (GoalInterval) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Composable
    fun getGoalTypeString(goalInterval: GoalInterval): String =
        when (goalInterval) {
            GoalInterval.Daily -> stringResource(SharedRes.strings.daily_interval_text)
            GoalInterval.Weekly -> stringResource(SharedRes.strings.weekly_interval_text)
            GoalInterval.Custom -> stringResource(SharedRes.strings.custom_interval_text)
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
                buttonColor = if (selectedGoalInterval == item) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (selectedGoalInterval == item) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                onButtonClicked = { onSelectGoalInterval(item) }
            )
        }
    }
}

@Composable
internal fun AddEditGoalItemTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    maxLines: Int = 1,
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
    apps: ImmutableList<AppInfo>,
    onEditApps: () -> Unit,
    modifier: Modifier = Modifier,
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
                            text = stringResource(SharedRes.strings.no_apps_text),
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
                            painter = painterResource(app.appIcon),
                            contentDescription = app.appName
                        )
                    }
                }
            }

            Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalScreenTimePicker(
    currentTimeMillis: Long,
    onUpdateCurrentTime: (timeMillis: Long) -> Unit,
    modifier: Modifier = Modifier,
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
        shape = MaterialTheme.shapes.large,
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
                text = stringResource(SharedRes.strings.app_time_limit),
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
                    text = if (goalType == GoalType.TasksGoal) {
                        stringResource(SharedRes.strings.select_number_of_tasks_txt)
                    } else {
                        stringResource(SharedRes.strings.target_value_txt)
                    },
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))

                ReluctTextField(
                    value = textValue,
                    isError = textValue.isBlank(),
                    errorText = stringResource(SharedRes.strings.invalid_value_text),
                    singleLine = true,
                    hint = if (goalType == GoalType.TasksGoal) {
                        stringResource(SharedRes.strings.enter_number_of_tasks_txt)
                    } else {
                        stringResource(SharedRes.strings.enter_target_value)
                    },
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
    currentDaysOfWeek: ImmutableList<Week>,
    goalInterval: GoalInterval,
    onDateTimeRangeChange: (ClosedRange<LocalDateTime>) -> Unit,
    onUpdateDaysOfWeek: (ImmutableList<Week>) -> Unit
) {
    when (goalInterval) {
        GoalInterval.Daily -> {
            item {
                AddEditGoalItemTitle(
                    modifier = Modifier
                        .animateItemPlacement(),
                    text = stringResource(SharedRes.strings.choose_days_txt),
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
                    text = stringResource(SharedRes.strings.start_time_txt),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                DateTimePills(
                    modifier = Modifier.animateItemPlacement(),
                    currentLocalDateTime = dateTimeRange.start,
                    onLocalDateTimeChange = { dateTime ->
                        val endTime = if (dateTime >= dateTimeRange.endInclusive) {
                            dateTime.plus(hours = 1)
                        } else {
                            dateTimeRange.endInclusive
                        }
                        onDateTimeRangeChange(dateTime..endTime)
                    }
                )
            }

            item {
                var endTimeError by remember { mutableStateOf(false) }

                AddEditGoalItemTitle(
                    modifier = Modifier
                        .animateItemPlacement(),
                    text = stringResource(SharedRes.strings.end_time_txt),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                DateTimePills(
                    modifier = Modifier.animateItemPlacement(),
                    hasError = endTimeError,
                    errorText = stringResource(SharedRes.strings.goal_time_error_text),
                    currentLocalDateTime = dateTimeRange.endInclusive,
                    onLocalDateTimeChange = { dateTime ->
                        endTimeError = dateTime <= dateTimeRange.start
                        val endTime =
                            if (dateTime <= dateTimeRange.start) {
                                dateTimeRange.endInclusive
                            } else {
                                dateTime
                            }
                        onDateTimeRangeChange(dateTimeRange.start..endTime)
                    }
                )
            }
        }
        else -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GoalNumberPicker(
    name: String,
    currentValue: Long,
    onUpdateCurrentValue: (Long) -> Unit,
    modifier: Modifier = Modifier,
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
    isSelected: Boolean,
    content: @Composable ColumnScope.() -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        content = content
    )
}
