package work.racka.reluct.ui.screens.screenTime.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.dialogs.MultiplatformAlertDialog
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.theme.Shapes

@Composable
internal fun UsagePermissionDialog(
    openDialog: State<Boolean>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (openDialog.value) {
        MultiplatformAlertDialog(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            isVisible = openDialog.value,
            onCloseDialog = onClose,
            title = {
                Text(text = stringResource(SharedRes.strings.open_settings_dialog_title))
            },
            text = {
                Text(text = stringResource(SharedRes.strings.usage_permissions_rationale_dialog_text))
            },
            confirmButton = {
                ReluctButton(
                    buttonText = stringResource(SharedRes.strings.ok),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        onClose()
                    }
                )
            },
            dismissButton = {
                ReluctButton(
                    buttonText = stringResource(SharedRes.strings.cancel),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onButtonClicked = onClose
                )
            }
        )
    }
}
