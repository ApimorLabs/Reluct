package work.racka.reluct.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import java.awt.Cursor

@OptIn(ExperimentalSplitPaneApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ReluctSplitPane(
    firstSlot: @Composable () -> Unit,
    secondSlot: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    splitPaneState: SplitPaneState = rememberSplitPaneState(),
) {
    HorizontalSplitPane(
        modifier = modifier,
        splitPaneState = splitPaneState
    ) {
        first(minSize = 400.dp, content = firstSlot)
        second(minSize = 300.dp, content = secondSlot)

        splitter {
            visiblePart {
                /*Box(
                    Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.background)
                )*/
            }
            handle {
                var isHovering by remember { mutableStateOf(false) }
                val handleAlpha = animateFloatAsState(if (isHovering) .2f else .05f)
                Box(
                    Modifier
                        .markAsHandle()
                        .cursorForHorizontalResize()
                        .width(24.dp)
                        .fillMaxHeight()
                        .onPointerEvent(PointerEventType.Enter, onEvent = { isHovering = true })
                        .onPointerEvent(PointerEventType.Exit, onEvent = { isHovering = false }),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .width(4.dp)
                            .fillMaxHeight(.95f)
                            .background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.onBackground
                                    .copy(alpha = handleAlpha.value)
                            )
                    )
                }
            }
        }
    }
}

private fun Modifier.cursorForHorizontalResize(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))
