package work.racka.reluct.compose.common.components.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FullScreenLoading(
    isLoadingProvider: () -> Boolean,
    modifier: Modifier = Modifier
) {
    val isVisible = remember { derivedStateOf { isLoadingProvider() } }
    AnimatedVisibility(
        modifier = modifier.fillMaxSize(),
        visible = isVisible.value,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
