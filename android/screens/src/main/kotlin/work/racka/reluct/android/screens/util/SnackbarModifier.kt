package work.racka.reluct.android.screens.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import work.racka.reluct.android.compose.components.util.ScrollContext

@Composable
internal fun getSnackbarModifier(mainPadding: PaddingValues, scrollContext: State<ScrollContext>) =
    remember {
        derivedStateOf {
            if (scrollContext.value.isTop) {
                Modifier.padding(bottom = mainPadding.calculateBottomPadding())
            } else Modifier.navigationBarsPadding()
        }
    }
