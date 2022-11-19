@file:JvmName("DateTimeDialogJvm")

package work.racka.reluct.compose.common.date.time.picker.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

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
