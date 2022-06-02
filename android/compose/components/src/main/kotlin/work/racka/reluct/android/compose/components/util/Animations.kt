package work.racka.reluct.android.compose.components.util

import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset

@Stable
fun slideInVerticallyReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )
): EnterTransition = slideInVertically(
    animationSpec = animationSpec,
    initialOffsetY = { fullHeight -> fullHeight }
)

@Stable
fun slideOutVerticallyReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )
): ExitTransition = slideOutVertically(
    animationSpec = animationSpec,
    targetOffsetY = { fullHeight -> fullHeight }
)

@Stable
fun slideInVerticallyFadeReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )
): EnterTransition = slideInVertically(
    animationSpec = animationSpec,
    initialOffsetY = { fullHeight -> fullHeight }
) + fadeIn()

@Stable
fun slideOutVerticallyFadeReversed(
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )
): ExitTransition = slideOutVertically(
    animationSpec = animationSpec,
    targetOffsetY = { fullHeight -> fullHeight }
) + fadeOut()