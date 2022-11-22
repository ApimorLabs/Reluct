package work.racka.reluct.android.screens.util

import androidx.compose.runtime.*
import work.racka.reluct.compose.common.components.util.ScrollContext

/**
 * Triggers [onFetchData] when [isFetchAllowedProvider] provides true. State that trigger fetching
 * should be captured in the [isFetchAllowedProvider] lambda and nowhere else.
 * This is defined as separate component to defer state reads and avoid recomposition
 */
@Composable
internal fun FetchMoreDataHandler(
    scrollContext: State<ScrollContext>,
    isFetchAllowedProvider: () -> Boolean,
    onFetchData: () -> Unit,
) {
    val isAtBottom = remember { derivedStateOf { scrollContext.value.isBottom } }
    LaunchedEffect(isAtBottom.value) {
        if (isFetchAllowedProvider() && isAtBottom.value) onFetchData()
    }
}
