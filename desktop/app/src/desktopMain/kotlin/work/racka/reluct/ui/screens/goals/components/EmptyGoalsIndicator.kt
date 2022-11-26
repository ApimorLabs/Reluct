package work.racka.reluct.ui.screens.goals.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
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
internal fun EmptyGoalsIndicator(
    showAnimationProvider: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    val showAnimation = remember { derivedStateOf(showAnimationProvider) }
    if (showAnimation.value) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ImageWithDescription(
                painter = painterResource(SharedRes.assets.add_files),
                imageSize = 200.dp,
                description = stringResource(SharedRes.strings.no_goals_text),
                descriptionTextStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
