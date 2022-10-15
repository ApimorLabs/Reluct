package work.racka.reluct.android.compose.components.cards.goal_entry

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.cards.card_with_actions.ReluctDescriptionCard
import work.racka.reluct.android.compose.components.textfields.texts.EntryDescription
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType
import work.racka.reluct.common.model.util.time.TimeUtils

@Composable
internal fun GoalIntervalLabel(
    modifier: Modifier = Modifier,
    goal: Goal,
    color: Color = LocalContentColor.current,
    includeExtraText: Boolean = true
) {
    val context = LocalContext.current
    val intervalText by remember(
        goal.goalDuration.goalInterval, goal.goalDuration.formattedTimeRange
    ) {
        derivedStateOf {
            when (goal.goalDuration.goalInterval) {
                GoalInterval.Daily -> context.getString(R.string.daily_interval_text)
                GoalInterval.Weekly -> context.getString(R.string.weekly_interval_text)
                GoalInterval.Custom -> {
                    val formatted = goal.goalDuration.formattedTimeRange
                    if (formatted != null) {
                        "${formatted.start} - ${formatted.endInclusive}"
                    } else context.getString(R.string.custom_interval_text)
                }
            }
        }
    }

    val fullText by remember(intervalText) {
        derivedStateOf {
            val preText = if (includeExtraText) context.getString(R.string.interval_text) + " : "
            else ""
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
    modifier: Modifier = Modifier,
    goalType: GoalType,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val context = LocalContext.current
    val goalTypeText by remember(goalType) {
        derivedStateOf {
            when (goalType) {
                GoalType.TasksGoal -> context.getString(R.string.tasks_goal_type)
                GoalType.AppScreenTimeGoal -> context.getString(R.string.app_screen_time_goal_type)
                GoalType.DeviceScreenTimeGoal -> context.getString(R.string.device_screen_time_goal_type)
                GoalType.NumeralGoal -> context.getString(R.string.numeral_goal_type)
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
                vertical = Dimens.SmallPadding.size, horizontal = Dimens.MediumPadding.size
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
    modifier: Modifier = Modifier,
    goal: Goal,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GoalTypeLabel(
            goalType = goal.goalType, containerColor = containerColor, contentColor = contentColor
        )
        Box(
            modifier = Modifier
                .clip(Shapes.large)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            GoalIntervalLabel(
                modifier = Modifier.padding(
                    vertical = Dimens.SmallPadding.size, horizontal = Dimens.MediumPadding.size
                ), goal = goal, color = contentColor, includeExtraText = false
            )
        }
    }
}

@Composable
fun GoalValuesCard(
    modifier: Modifier = Modifier,
    goal: Goal,
    isLoading: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onUpdateClicked: (GoalType) -> Unit
) {
    val context = LocalContext.current
    val targetValueText by remember(goal.targetValue) {
        derivedStateOf {
            when (goal.goalType) {
                GoalType.AppScreenTimeGoal -> TimeUtils
                    .getFormattedTimeDurationString(goal.targetValue)
                GoalType.DeviceScreenTimeGoal -> TimeUtils
                    .getFormattedTimeDurationString(goal.targetValue)
                GoalType.TasksGoal -> context.resources.getQuantityString(
                    R.plurals.tasks_text,
                    goal.targetValue.toInt(),
                    goal.targetValue
                )
                else -> goal.targetValue
            }
        }
    }

    val currentValueText by remember(goal.currentValue) {
        derivedStateOf {
            when (goal.goalType) {
                GoalType.AppScreenTimeGoal -> TimeUtils
                    .getFormattedTimeDurationString(goal.currentValue)
                GoalType.DeviceScreenTimeGoal -> TimeUtils
                    .getFormattedTimeDurationString(goal.currentValue)
                GoalType.TasksGoal -> context.resources.getQuantityString(
                    R.plurals.tasks_text,
                    goal.currentValue.toInt(),
                    goal.currentValue
                )
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
                        id = R.string.current_value_value_text,
                        currentValueText
                    )
                )
                EntryDescription(
                    text = stringResource(
                        id = R.string.target_value_value_text,
                        targetValueText
                    )
                )
            }
        },
        description = {},
        onClick = { onUpdateClicked(goal.goalType) },
        rightActions = {
            IconButton(onClick = { onUpdateClicked(goal.goalType) }, enabled = !isLoading) {
                if (goal.goalType == GoalType.NumeralGoal) {
                    Icon(
                        imageVector = Icons.Rounded.Edit,
                        contentDescription = stringResource(id = R.string.edit_button_text)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = stringResource(id = R.string.sync_text)
                    )
                }
            }
        }
    )
}
