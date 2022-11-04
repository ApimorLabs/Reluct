package work.racka.reluct.android.screens.goals.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import work.racka.reluct.android.compose.components.animations.slideInVerticallyFadeReversed
import work.racka.reluct.android.compose.components.animations.slideOutVerticallyFadeReversed
import work.racka.reluct.android.compose.components.buttons.ReluctFloatingActionButton
import work.racka.reluct.android.compose.components.util.ScrollContext
import work.racka.reluct.android.screens.R

@Composable
internal fun NewGoalFloatingButton(
    scrollContextState: State<ScrollContext>,
    mainScaffoldPadding: PaddingValues,
    onClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = scrollContextState.value.isTop,
        enter = slideInVerticallyFadeReversed(),
        exit = slideOutVerticallyFadeReversed()
    ) {
        ReluctFloatingActionButton(
            modifier = Modifier
                .padding(bottom = mainScaffoldPadding.calculateBottomPadding()),
            buttonText = stringResource(R.string.new_goal_text),
            contentDescription = stringResource(R.string.add_icon),
            icon = Icons.Rounded.Add,
            onButtonClicked = onClick
        )
    }
}