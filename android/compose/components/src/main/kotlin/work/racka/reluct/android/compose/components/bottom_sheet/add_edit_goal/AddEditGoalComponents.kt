package work.racka.reluct.android.compose.components.bottom_sheet.add_edit_goal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.buttons.ReluctButton
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

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
    ) {
        items(GoalType.values()) { type ->
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
    color: Color = LocalContentColor.current
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SelectedAppsCard(
    modifier: Modifier = Modifier,
    apps: List<AppInfo>,
    onEditApps: () -> Unit,
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
                modifier = modifier
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

@OptIn(ExperimentalMaterial3Api::class)
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

@OptIn(ExperimentalFoundationApi::class)
internal fun LazyListScope.goalTargetValuePicker(
    goalType: GoalType,
    targetValue: Long,
    onUpdateTargetValue: (Long) -> Unit
) {
    if (goalType == GoalType.DeviceScreenTimeGoal || goalType == GoalType.AppScreenTimeGoal) {
        item {
            AddEditGoalItemTitle(
                modifier = Modifier
                    .animateItemPlacement(),
                text = stringResource(R.string.app_time_limit),
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            GoalScreenTimePicker(
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
                )

                Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))

                ReluctTextField(
                    value = textValue,
                    isError = textValue.isBlank(),
                    hint = if (goalType == GoalType.TasksGoal) stringResource(R.string.enter_number_of_tasks_txt)
                    else stringResource(R.string.enter_target_value),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
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
                        .fillMaxWidth(.4f)
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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