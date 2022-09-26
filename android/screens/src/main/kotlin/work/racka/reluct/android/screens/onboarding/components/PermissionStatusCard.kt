package work.racka.reluct.android.screens.onboarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PermissionStatusCard(
    modifier: Modifier = Modifier,
    isGranted: Boolean,
    onPermissionRequest: () -> Unit
) {

    val containerColor by animateColorAsState(
        targetValue = if (isGranted) Color.Green.copy(alpha = .7f) else Color.Red.copy(alpha = .7f)
    )
    val contentColor by animateColorAsState(
        targetValue = if (isGranted) Color.Black.copy(alpha = .8f) else Color.White
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onPermissionRequest,
        shape = Shapes.large,
        modifier = Modifier
            .fillMaxWidth() then modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = if (isGranted) Icons.Rounded.CheckCircle else Icons.Rounded.Error,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))

            Text(
                text = if (isGranted) {
                    stringResource(id = R.string.permission_granted_text)
                } else stringResource(id = R.string.click_to_grant_text),
                style = MaterialTheme.typography.titleLarge,
                color = contentColor
            )
        }
    }
}