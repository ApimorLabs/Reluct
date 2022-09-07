package work.racka.reluct.android.compose.components.topBar.toolbar

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.topBar.toolbar.scrollflags.ScrollState

val minToolbarHeight = 96.dp
val maxToolbarHeight = 176.dp

@Stable
interface ToolbarState {
    val offset: Float
    val height: Float
    val progress: Float
    val consumed: Float
    var scrollTopLimitReached: Boolean
    var scrollOffset: Float
    val translationY: Float
        get() = height + offset
}

abstract class ScrollFlagState(heightRange: IntRange) : ToolbarState {
    init {
        require(heightRange.first >= 0 && heightRange.last >= heightRange.first) {
            "The lowest height value must be >= 0 and the highest height value must be >= the lowest value."
        }
    }

    protected val minHeight = heightRange.first
    protected val maxHeight = heightRange.last
    protected val rangeDifference = maxHeight - minHeight
    protected var _consumed: Float = 0f

    protected abstract var _scrollOffset: Float

    final override val height: Float
        get() = (maxHeight - scrollOffset).coerceIn(minHeight.toFloat(), maxHeight.toFloat())

    final override val progress: Float
        get() = 1 - (maxHeight - height) / rangeDifference

    final override val consumed: Float
        get() = _consumed

    final override var scrollTopLimitReached: Boolean = true
}

abstract class FixedScrollFlagState(heightRange: IntRange) : ScrollFlagState(heightRange) {
    final override val offset: Float = 0f

}

@Composable
fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(
            heightRange = toolbarHeightRange
        )
    }
}

@Composable
fun rememberToolbarNestedScrollConnection(
    toolbarState: ToolbarState,
    listState: LazyListState
): NestedScrollConnection {
    return remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached = listState.firstVisibleItemIndex == 0 &&
                        listState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                // Returning Zero so we just observe the scroll but don't execute it
                return Offset(x = 0f, y = toolbarState.consumed)
            }
        }
    }
}

@Composable
private fun TestCollapse() {
    val listState = rememberLazyListState()
    val toolbarHeightRange = with(LocalDensity.current) {
        minToolbarHeight.roundToPx()..maxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange = toolbarHeightRange)

    val scrollConnection = rememberToolbarNestedScrollConnection(
        toolbarState = toolbarState,
        listState = listState
    )

}