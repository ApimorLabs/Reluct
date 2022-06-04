package work.racka.reluct.android.compose.components.cards.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsCard(
    modifier: Modifier = Modifier,
    imageSlot: @Composable () -> Unit,
    permissionDetails: String,
    onPermissionRequest: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    shape: Shape = Shapes.large,
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .padding(horizontal = Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
        ) {
            imageSlot()

            Text(
                text = permissionDetails,
                style = MaterialTheme.typography.bodyLarge,
                color = LocalContentColor.current,
                textAlign = TextAlign.Center
            )

            ReluctButton(
                buttonText = stringResource(R.string.allow_permission),
                icon = Icons.Rounded.AutoAwesome,
                onButtonClicked = onPermissionRequest
            )
        }
    }
}