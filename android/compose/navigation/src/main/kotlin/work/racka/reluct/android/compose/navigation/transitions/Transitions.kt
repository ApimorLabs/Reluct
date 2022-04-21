package work.racka.reluct.android.compose.navigation.transitions

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

// Enter transition when you navigate to a screen
@ExperimentalAnimationApi
fun scaleInEnterTransition() = scaleIn(
    initialScale = .9f,
    animationSpec = tween(500)
) + fadeIn(
    animationSpec = tween(300)
)

// Exit transition when you navigate to a screen
@ExperimentalAnimationApi
fun scaleOutExitTransition() = scaleOut(
    targetScale = 1.1f,
    animationSpec = tween(300)
) + fadeOut(
    animationSpec = tween(300)
)

// Enter transition of a screen when you pop to it
@ExperimentalAnimationApi
fun scaleInPopEnterTransition() = scaleIn(
    initialScale = 1.1f,
    animationSpec = tween(500)
) + fadeIn(
    animationSpec = tween(300)
)

// Exit transition of a screen you are popping from
@ExperimentalAnimationApi
fun scaleOutPopExitTransition() = scaleOut(
    targetScale = .9f,
    animationSpec = tween(300)
) + fadeOut(
    animationSpec = tween(300)
)