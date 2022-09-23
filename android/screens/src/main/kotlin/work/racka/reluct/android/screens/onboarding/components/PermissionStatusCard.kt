package work.racka.reluct.android.screens.onboarding.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import work.racka.reluct.android.compose.components.cards.card_with_actions.ReluctDescriptionCard
import work.racka.reluct.android.screens.R

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
        targetValue = if (isGranted) Color.Black else Color.White
    )

    ReluctDescriptionCard(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        title = {
            Text(
                text = if (isGranted) {
                    stringResource(id = R.string.permission_granted_text)
                } else stringResource(id = R.string.click_to_grant_text),
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
        },
        description = { },
        icon = if (isGranted) Icons.Rounded.CheckCircle else Icons.Rounded.Error,
        onClick = onPermissionRequest
    )
}