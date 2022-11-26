package work.racka.reluct.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.SplitPaneState
import org.jetbrains.compose.splitpane.rememberSplitPaneState
import java.awt.Cursor

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun ReluctSplitPane(
    firstSlot: @Composable () -> Unit,
    secondSlot: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    splitPaneState: SplitPaneState = rememberSplitPaneState(initialPositionPercentage = .5f)
) {
    HorizontalSplitPane(
        modifier = modifier,
        splitPaneState = splitPaneState
    ) {
        first(minSize = 400.dp, content = firstSlot)
        second(minSize = 500.dp, content = secondSlot)

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
                Box(
                    Modifier
                        .markAsHandle()
                        .cursorForHorizontalResize()
                        .background(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .2f),
                            shape = CircleShape
                        )
                        .width(4.dp)
                        .fillMaxHeight(.8f)
                )
            }
        }
    }
}

private fun Modifier.cursorForHorizontalResize(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))
