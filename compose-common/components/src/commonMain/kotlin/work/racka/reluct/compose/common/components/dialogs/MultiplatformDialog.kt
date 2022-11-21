package work.racka.reluct.compose.common.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Stable
data class MultiplatformDialogProperties(
    val desktopWindowConfig: DesktopWindowConfig = DesktopWindowConfig(),
    val androidWindowConfig: AndroidWindowConfig = AndroidWindowConfig()
)

@Stable
data class AndroidWindowConfig(
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
)

@Stable
data class DesktopWindowConfig(
    val desktopWindowSize: DpSize = DpSize(400.dp, 500.dp),
    val title: String = "Untitled",
    val icon: Painter? = null,
    val undecorated: Boolean = false,
    val transparent: Boolean = false,
    val resizable: Boolean = true,
    val enabled: Boolean = true,
    val focusable: Boolean = true,
    val onPreviewKeyEvent: ((KeyEvent) -> Boolean) = { false },
    val onKeyEvent: ((KeyEvent) -> Boolean) = { false },
)

@Composable
expect fun MultiplatformDialog(
    onCloseDialog: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    properties: MultiplatformDialogProperties = MultiplatformDialogProperties(),
    content: @Composable () -> Unit = {},
)

@Composable
expect fun MultiplatformAlertDialog(
    onCloseDialog: () -> Unit,
    isVisible: Boolean,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(containerColor),
    properties: MultiplatformDialogProperties = MultiplatformDialogProperties(),
)
