package work.racka.reluct.android.compose.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import work.racka.reluct.compose.common.components.dialogs.MultiplatformDialog
import work.racka.reluct.compose.common.components.dialogs.MultiplatformDialogProperties
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@Composable
fun CircularProgressDialog(
    isVisible: State<Boolean>,
    onDismiss: () -> Unit,
    loadingText: String,
    modifier: Modifier = Modifier,
    properties: MultiplatformDialogProperties = MultiplatformDialogProperties(),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    MultiplatformDialog(
        isVisible = isVisible.value,
        onCloseDialog = onDismiss,
        properties = properties
    ) {
        Surface(
            modifier = modifier,
            shape = Shapes.large,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.MediumPadding.size),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
            ) {
                CircularProgressIndicator()

                Text(
                    modifier = Modifier.weight(1f),
                    text = loadingText,
                    style = MaterialTheme.typography.bodyLarge
                        .copy(color = contentColor)
                )
            }
        }
    }
}
