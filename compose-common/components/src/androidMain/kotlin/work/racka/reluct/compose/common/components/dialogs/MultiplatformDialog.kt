@file:JvmName("MultiplatformDialogJvm")

package work.racka.reluct.compose.common.components.dialogs

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
actual fun MultiplatformDialog(
    onCloseDialog: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier,
    properties: MultiplatformDialogProperties,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onCloseDialog,
            properties = DialogProperties(
                dismissOnBackPress = properties.androidWindowConfig.dismissOnBackPress,
                dismissOnClickOutside = properties.androidWindowConfig.dismissOnClickOutside
            ),
            content = content
        )
    }
}

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
    properties: MultiplatformDialogProperties,
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onCloseDialog,
            modifier = modifier,
            title = title,
            text = text,
            icon = icon,
            shape = shape,
            containerColor = containerColor,
            titleContentColor = contentColor,
            iconContentColor = contentColor,
            textContentColor = contentColor,
            confirmButton = confirmButton,
            dismissButton = dismissButton,
            properties = DialogProperties(
                dismissOnBackPress = properties.androidWindowConfig.dismissOnBackPress,
                dismissOnClickOutside = properties.androidWindowConfig.dismissOnClickOutside
            )
        )
    }
}