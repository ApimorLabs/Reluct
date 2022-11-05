package work.racka.reluct.android.compose.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.theme.Shapes

@Composable
fun DiscardPromptDialog(
    dialogTitleProvider: () -> String,
    openDialog: State<Boolean>,
    onClose: () -> Unit,
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (openDialog.value) {
        val title = remember { derivedStateOf(dialogTitleProvider) }
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onClose,
            title = {
                Text(text = title.value)
            },
            text = {
                Text(text = stringResource(R.string.discard_task_message))
            },
            confirmButton = {
                ReluctButton(
                    buttonText = stringResource(R.string.ok),
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
                    buttonText = stringResource(R.string.cancel),
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
