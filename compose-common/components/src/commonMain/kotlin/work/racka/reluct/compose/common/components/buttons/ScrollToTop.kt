package work.racka.reluct.android.compose.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import work.racka.reluct.android.compose.components.util.ScrollContext
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.util.navigationBarsPadding
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BoxScope.ScrollToTop(
    scrollContext: State<ScrollContext>,
    onScrollToTop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        modifier = modifier.align(Alignment.BottomCenter),
        visible = !scrollContext.value.isTop,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        ReluctFloatingActionButton(
            modifier = Modifier
                .padding(bottom = Dimens.MediumPadding.size)
                .navigationBarsPadding(),
            buttonText = "",
            contentDescription = stringResource(SharedRes.strings.scroll_to_top),
            icon = Icons.Rounded.ArrowUpward,
            onButtonClicked = onScrollToTop,
            expanded = false
        )
    }
}
