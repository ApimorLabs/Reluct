package work.racka.reluct.ui.screens.tasks.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.images.ImageWithDescription
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.resources.stringResource

@Composable
internal fun FullEmptyTasksIndicator(
    showAnimationProvider: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    val showAnimation = remember { derivedStateOf(showAnimationProvider) }
    AnimatedVisibility(
        visible = showAnimation.value,
        modifier = modifier.fillMaxSize(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ImageWithDescription(
                painter = painterResource(SharedRes.assets.empty),
                description = stringResource(SharedRes.strings.no_tasks_text),
                imageSize = 200.dp,
            )
        }
    }
}
