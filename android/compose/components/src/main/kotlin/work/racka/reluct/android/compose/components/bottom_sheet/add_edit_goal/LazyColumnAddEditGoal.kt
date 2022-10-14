package work.racka.reluct.android.compose.components.bottom_sheet.add_edit_goal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.textfields.ReluctTextField
import work.racka.reluct.android.compose.components.util.PreviewData
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.ReluctAppTheme
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.common.model.util.time.TimeUtils.plus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnAddEditGoal(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    goal: Goal,
    onUpdateGoal: (Goal) -> Unit,
    onDiscard: () -> Unit,
    onSave: (goal: Goal) -> Unit,
    onShowAppPicker: () -> Unit
) {

    var goalNameError by remember { mutableStateOf(false) }

    val localDateTimeRange by remember(goal.goalDuration.timeRangeInMillis) {
        derivedStateOf {
            goal.goalDuration.timeRangeInMillis?.let { range ->
                val start = TimeUtils.epochMillisToLocalDateTime(range.first)
                val end = TimeUtils.epochMillisToLocalDateTime(range.last)
                start..end
            } ?: run {
                val start = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                val end = start.plus(days = 1)
                return@run start..end
            }
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
                value = goal.name,
                hint = stringResource(R.string.goal_name_text),
                isError = goalNameError,
                errorText = stringResource(R.string.goal_name_error_text),
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                onTextChange = { text ->
                    onUpdateGoal(goal.copy(name = text))
                }
            )
        }

        item {
            ReluctTextField(
                modifier = Modifier
                    .height(200.dp),
                value = goal.description,
                hint = stringResource(R.string.description_hint),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                onTextChange = { text ->
                    onUpdateGoal(goal.copy(description = text))
                }
            )
        }

        // Goal Type Selector
        item {
            AddEditGoalItemTitle(text = stringResource(R.string.goal_type_text))
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            GoalTypeSelector(
                selectedGoalType = goal.goalType,
                onSelectGoalType = { type -> onUpdateGoal(goal.copy(goalType = type)) }
            )
        }

        // Show Apps Selector
        if (goal.goalType == GoalType.AppScreenTimeGoal) {
            item {
                AddEditGoalItemTitle(
                    modifier = Modifier
                        .animateItemPlacement(),
                    text = stringResource(R.string.app_list_header),
                )
                Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
                SelectedAppsCard(
                    modifier = Modifier.animateItemPlacement(),
                    apps = goal.relatedApps,
                    onEditApps = onShowAppPicker
                )
            }
        }

        // Target Value Manipulation
        goalTargetValuePicker(
            goalType = goal.goalType,
            targetValue = goal.targetValue,
            onUpdateTargetValue = { onUpdateGoal(goal.copy(targetValue = it)) }
        )

        // Goal Interval Selector
        item {
            AddEditGoalItemTitle(
                modifier = Modifier.animateItemPlacement(),
                text = stringResource(R.string.interval_text)
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            GoalIntervalSelector(
                modifier = Modifier.animateItemPlacement(),
                selectedGoalInterval = goal.goalDuration.goalInterval,
                onSelectGoalInterval = { interval ->
                    val duration = goal.goalDuration.let {
                        if (interval == GoalInterval.Custom) {
                            it.copy(goalInterval = interval)
                        } else it.copy(goalInterval = interval, timeRangeInMillis = null)
                    }
                    onUpdateGoal(goal.copy(goalDuration = duration))
                }
            )
        }

        // Goal Interval Duration Selection
        goalDurationPicker(
            dateTimeRange = localDateTimeRange,
            currentDaysOfWeek = goal.goalDuration.selectedDaysOfWeek,
            goalInterval = goal.goalDuration.goalInterval,
            onDateTimeRangeChange = { dateTimeRange ->
                val start = dateTimeRange.start.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
                val end = dateTimeRange.endInclusive.toInstant(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
                val duration = goal.goalDuration.copy(timeRangeInMillis = start..end)
                onUpdateGoal(goal.copy(goalDuration = duration))
            },
            onUpdateDaysOfWeek = { daysOfWeek ->
                val duration = goal.goalDuration.copy(selectedDaysOfWeek = daysOfWeek)
                onUpdateGoal(goal.copy(goalDuration = duration))
            }
        )

        // Buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItemPlacement(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReluctButton(
                    buttonText = stringResource(id = R.string.discard_button_text),
                    icon = Icons.Rounded.DeleteSweep,
                    onButtonClicked = onDiscard,
                    shape = Shapes.large,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    buttonColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
                ReluctButton(
                    buttonText = stringResource(id = R.string.save_button_text),
                    icon = Icons.Rounded.Save,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        val isTitleBlank = goal.name.isBlank()
                        goalNameError = isTitleBlank
                        if (!isTitleBlank) onSave(goal)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddEditGoalPreview() {
    ReluctAppTheme {
        Surface(color = MaterialTheme.colorScheme.background.copy(.7f)) {
            LazyColumnAddEditGoal(
                goal = PreviewData.goals.last(),
                onUpdateGoal = {},
                onDiscard = {},
                onSave = {},
                onShowAppPicker = {}
            )
        }
    }
}
