@file:JvmName("DateTimeDialogJvm")

package work.racka.reluct.compose.common.date.time.picker.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState

@Composable
internal actual fun MultiplatformDialog(
    onCloseDialog: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier,
    properties: DateTimeDialogProperties,
    content: @Composable () -> Unit
) {
    val dialogState = rememberDialogState(
        position = WindowPosition(Alignment.Center),
        size = properties.desktopWindowSize
    )
    Dialog(
        onCloseRequest = onCloseDialog,
        state = dialogState,
        visible = isVisible,
        resizable = true,
        undecorated = true,
        transparent = true
    ) {
        content()
    }
}
