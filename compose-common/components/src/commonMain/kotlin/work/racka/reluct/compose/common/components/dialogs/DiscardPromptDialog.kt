package work.racka.reluct.android.compose.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.dialogs.MultiplatformAlertDialog
import work.racka.reluct.compose.common.components.dialogs.MultiplatformDialogProperties
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.theme.Shapes

@Composable
fun DiscardPromptDialog(
    dialogTitleProvider: () -> String,
    openDialog: State<Boolean>,
    onClose: () -> Unit,
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
    properties: MultiplatformDialogProperties = MultiplatformDialogProperties()
) {
    val title = remember { derivedStateOf(dialogTitleProvider) }
    MultiplatformAlertDialog(
        isVisible = openDialog.value,
        modifier = modifier,
        onCloseDialog = onClose,
        properties = properties,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Text(text = title.value)
        },
        text = {
            Text(text = stringResource(SharedRes.strings.discard_task_message))
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
                    onGoBack()
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
