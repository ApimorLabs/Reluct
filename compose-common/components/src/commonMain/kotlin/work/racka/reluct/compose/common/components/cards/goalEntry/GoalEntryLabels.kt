package work.racka.reluct.compose.common.components.cards.goalEntry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.time.TimeUtils
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.cards.cardWithActions.ReluctDescriptionCard
import work.racka.reluct.compose.common.components.resources.pluralStringResource
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.textfields.texts.EntryDescription
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@Composable
internal fun GoalIntervalLabel(
    goal: Goal,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    includeExtraText: Boolean = true
) {
    val dailyIntervalText = stringResource(SharedRes.strings.daily_interval_text)
    val weeklyIntervalText = stringResource(SharedRes.strings.weekly_interval_text)
    val customIntervalText = stringResource(SharedRes.strings.custom_interval_text)
    val intervalPreText = stringResource(SharedRes.strings.interval_text)

    val intervalText by remember(
        goal.goalDuration.goalInterval,
        goal.goalDuration.formattedTimeRange
    ) {
        derivedStateOf {
            when (goal.goalDuration.goalInterval) {
                GoalInterval.Daily -> dailyIntervalText
                GoalInterval.Weekly -> weeklyIntervalText
                GoalInterval.Custom -> {
                    val formatted = goal.goalDuration.formattedTimeRange
                    if (formatted != null) {
                        "${formatted.start} - ${formatted.endInclusive}"
                    } else {
                        customIntervalText
                    }
                }
            }
        }
    }

    val fullText by remember(intervalText) {
        derivedStateOf {
            val preText = if (includeExtraText) {
                "$intervalPreText : "
            } else {
                ""
            }
            preText + intervalText
        }
    }
    Text(
        modifier = modifier,
        text = fullText,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = color.copy(alpha = .8f)
    )
}

@Composable
internal fun GoalTypeLabel(
    goalType: GoalType,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    val tasksGoalTypeText = stringResource(SharedRes.strings.tasks_goal_type)
    val appGoalTypeText = stringResource(SharedRes.strings.app_screen_time_goal_type)
    val deviceGoalTypeText = stringResource(SharedRes.strings.device_screen_time_goal_type)
    val numeralGoalTypeText = stringResource(SharedRes.strings.numeral_goal_type)

    val goalTypeText by remember(goalType) {
        derivedStateOf {
            when (goalType) {
                GoalType.TasksGoal -> tasksGoalTypeText
                GoalType.AppScreenTimeGoal -> appGoalTypeText
                GoalType.DeviceScreenTimeGoal -> deviceGoalTypeText
                GoalType.NumeralGoal -> numeralGoalTypeText
            }
        }
    }

    Box(
        modifier = Modifier
            .clip(Shapes.large)
            .background(containerColor) then modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = Dimens.SmallPadding.size,
                horizontal = Dimens.MediumPadding.size
            ),
            text = goalTypeText,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun GoalTypeAndIntervalLabels(
    goal: Goal,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GoalTypeLabel(
            goalType = goal.goalType,
            containerColor = containerColor,
            contentColor = contentColor
        )
        Box(
            modifier = Modifier
                .clip(Shapes.large)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            GoalIntervalLabel(
                modifier = Modifier.padding(
                    vertical = Dimens.SmallPadding.size,
                    horizontal = Dimens.MediumPadding.size
                ),
                goal = goal,
                color = contentColor,
                includeExtraText = false
            )
        }
    }
}

@Composable
fun GoalValuesCard(
    goal: Goal,
    onUpdateClicked: (GoalType) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val tasksPlurals = pluralStringResource(
        SharedRes.plurals.tasks_text,
        goal.targetValue.toInt(),
        goal.targetValue
    )

    val targetValueText by remember(goal.targetValue) {
        derivedStateOf {
            when (goal.goalType) {
                GoalType.AppScreenTimeGoal ->
                    TimeUtils.getFormattedTimeDurationString(goal.targetValue)
                GoalType.DeviceScreenTimeGoal ->
                    TimeUtils.getFormattedTimeDurationString(goal.targetValue)
                GoalType.TasksGoal -> tasksPlurals
                else -> goal.targetValue
            }
        }
    }

    val currentTasksPlurals = pluralStringResource(
        SharedRes.plurals.tasks_text,
        goal.currentValue.toInt(),
        goal.currentValue
    )
    val currentValueText by remember(goal.currentValue) {
        derivedStateOf {
            when (goal.goalType) {
                GoalType.AppScreenTimeGoal ->
                    TimeUtils.getFormattedTimeDurationString(goal.currentValue)
                GoalType.DeviceScreenTimeGoal ->
                    TimeUtils.getFormattedTimeDurationString(goal.currentValue)
                GoalType.TasksGoal -> currentTasksPlurals
                else -> goal.currentValue
            }
        }
    }

    ReluctDescriptionCard(
        modifier = modifier,
        contentColor = contentColor,
        containerColor = containerColor,
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)) {
                EntryDescription(
                    text = stringResource(
                        SharedRes.strings.current_value_value_text,
                        currentValueText
                    )
                )
                EntryDescription(
                    text = stringResource(
                        SharedRes.strings.target_value_value_text,
                        targetValueText
                    )
                )
            }
        },
        description = {},
        onClick = { onUpdateClicked(goal.goalType) },
        rightItems = {
            IconButton(onClick = { onUpdateClicked(goal.goalType) }, enabled = !isLoading) {
                if (goal.goalType == GoalType.NumeralGoal) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = stringResource(SharedRes.strings.edit_button_text)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = stringResource(SharedRes.strings.sync_text)
                    )
                }
            }
        }
    )
}
