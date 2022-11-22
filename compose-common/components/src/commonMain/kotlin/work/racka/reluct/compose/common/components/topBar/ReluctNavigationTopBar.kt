package work.racka.reluct.compose.common.components.topBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import work.racka.reluct.compose.common.theme.Dimens

@Composable
fun ReluctNavigationTopBar(
    topContent: @Composable BoxScope.() -> Unit,
    navigationContent: @Composable ColumnScope.() -> Unit,
    topContentVisible: Boolean,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .background(color = backgroundColor),
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = horizontalAlignment
    ) {
        AnimatedVisibility(
            visible = topContentVisible,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center,
                content = topContent
            )
        }

        navigationContent()
    }
}
