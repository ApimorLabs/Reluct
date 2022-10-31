package work.racka.reluct.android.compose.components.cards.goal_entry

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.cards.card_with_actions.ReluctSwitchCard
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.goals.Goal

@Composable
fun GoalHeadingSwitchCard(
    goal: Goal,
    modifier: Modifier = Modifier,
    onToggleActiveState: (goalId: String, isActive: Boolean) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    shape: Shape = Shapes.large
) {
    val progressValue by remember(goal.currentValue) {
        derivedStateOf {
            if (goal.targetValue <= 0) {
                0f
            } else {
                1f * goal.currentValue / goal.targetValue
            }
        }
    }
    val progress = remember { Animatable(initialValue = 0f) }
    val progressColor by animateColorAsState(
        targetValue = if (progressValue > 1f) {
            MaterialTheme.colorScheme.error.copy(alpha = .7f)
        } else {
            MaterialTheme.colorScheme.primary.copy(alpha = .3f)
        }
    )
    val cardContentColor = remember { Animatable(contentColor) }
    val onErrorColor = MaterialTheme.colorScheme.onError

    LaunchedEffect(progressValue) {
        launch {
            progress.animateTo(
                targetValue = progressValue,
                animationSpec = TweenSpec(durationMillis = 1000)
            )
        }
        launch {
            cardContentColor.animateTo(
                targetValue = if (progressValue > 1f) {
                    onErrorColor
                } else {
                    contentColor
                },
                animationSpec = TweenSpec(durationMillis = 1000)
            )
        }
    }

    Box(
        modifier = Modifier
            .clip(shape)
            .background(containerColor)
            then modifier,
        contentAlignment = Alignment.Center
    ) {
        ReluctSwitchCard(
            modifier = Modifier
                .drawBehind {
                    val strokeWidth = size.height
                    drawLinearIndicator(0f, progress.value, progressColor, strokeWidth)
                },
            checked = goal.isActive,
            onCheckedChange = { onToggleActiveState(goal.id, it) },
            title = {
                Text(
                    modifier = Modifier,
                    text = goal.name,
                    style = textStyle,
                    color = LocalContentColor.current
                )
            },
            description = {},
            bottomContent = {
                Divider(
                    modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
                    color = LocalContentColor.current
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = Dimens.MediumPadding.size)
                        .padding(
                            bottom = Dimens.MediumPadding.size,
                            top = Dimens.SmallPadding.size
                        ),
                    text = goal.description.ifBlank { stringResource(R.string.no_description_text) },
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalContentColor.current
                )
            },
            onClick = { onToggleActiveState(goal.id, !goal.isActive) },
            containerColor = Color.Transparent,
            contentColor = cardContentColor.value,
            shape = shape
        )
    }
}
