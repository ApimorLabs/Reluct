package work.racka.reluct.android.compose.components.cards.goal_entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType

@Composable
internal fun GoalIntervalLabel(
    modifier: Modifier = Modifier,
    goal: Goal,
    color: Color = LocalContentColor.current
) {
    val context = LocalContext.current
    val intervalText by remember(
        goal.goalDuration.goalInterval,
        goal.goalDuration.formattedTimeRange
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
    Text(
        text = stringResource(id = R.string.interval_text) + " : $intervalText",
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
                GoalType.TasksGoal -> context.getString(R.string.tasks_goal_interval)
                GoalType.AppScreenTimeGoal ->
                    context.getString(R.string.app_screen_time_goal_interval)
                GoalType.DeviceScreenTimeGoal ->
                    context.getString(R.string.app_screen_time_goal_interval)
                GoalType.NumeralGoal -> context.getString(R.string.numeral_goal_interval)
            }
        }
    }

    Box(
        modifier = Modifier
            .clip(Shapes.large)
            .background(containerColor)
                then modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                vertical = Dimens.ExtraSmallPadding.size,
                horizontal = Dimens.SmallPadding.size
            ),
            text = goalTypeText,
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
