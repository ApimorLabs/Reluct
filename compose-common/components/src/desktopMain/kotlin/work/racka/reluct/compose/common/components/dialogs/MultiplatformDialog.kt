@file:JvmName("MultiplatformDesktopJvm")

package work.racka.reluct.compose.common.components.dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState

@Composable
actual fun MultiplatformDialog(
    onCloseDialog: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier,
    properties: MultiplatformDialogProperties,
    content: @Composable () -> Unit
) {
    val dialogState = rememberDialogState(
        position = WindowPosition(Alignment.Center),
        size = properties.desktopWindowConfig.desktopWindowSize
    )
    Dialog(
        onCloseRequest = onCloseDialog,
        state = dialogState,
        visible = isVisible,
        title = properties.desktopWindowConfig.title,
        icon = properties.desktopWindowConfig.icon,
        resizable = properties.desktopWindowConfig.resizable,
        undecorated = properties.desktopWindowConfig.undecorated,
        transparent = properties.desktopWindowConfig.transparent,
        enabled = properties.desktopWindowConfig.enabled,
        focusable = properties.desktopWindowConfig.focusable,
        onPreviewKeyEvent = properties.desktopWindowConfig.onPreviewKeyEvent,
        onKeyEvent = properties.desktopWindowConfig.onKeyEvent
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
actual fun MultiplatformAlertDialog(
    onCloseDialog: () -> Unit,
    isVisible: Boolean,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    modifier: Modifier,
    icon: @Composable () -> Unit,
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    properties: MultiplatformDialogProperties
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onCloseDialog,
            modifier = modifier,
            title = title,
            text = text,
            shape = shape,
            backgroundColor = containerColor,
            contentColor = contentColor,
            confirmButton = confirmButton,
            dismissButton = dismissButton
        )
    }
}
