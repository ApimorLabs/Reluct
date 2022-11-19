@file:JvmName("DateTimeDialogJvm")

package work.racka.reluct.compose.common.date.time.picker.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/*@Composable
internal actual fun DateTimeDialog(
    onCloseDialog: () -> Unit,
    onPositiveButtonClicked: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier,
    properties: DateTimeDialogProperties,
    containerColor: Color,
    shape: Shape,
    positiveButtonText: String?,
    negativeButtonText: String?,
    content: @Composable () -> Unit,
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onCloseDialog,
            properties = DialogProperties(
                dismissOnBackPress = properties.dismissOnBackPress,
                dismissOnClickOutside = properties.dismissOnClickOutside
            )
        ) {
            DialogContentHolder(
                positiveButtonClicked = onPositiveButtonClicked,
                negativeButtonClicked = onCloseDialog,
                content = content,
                containerColor = containerColor,
                shape = shape,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText
            )
        }
    }
}*/

@Composable
internal actual fun MultiplatformDialog(
    onCloseDialog: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier,
    properties: DateTimeDialogProperties,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onCloseDialog,
            properties = DialogProperties(
                dismissOnBackPress = properties.dismissOnBackPress,
                dismissOnClickOutside = properties.dismissOnClickOutside
            ),
            content = content
        )
    }
}