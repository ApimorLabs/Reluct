package work.racka.reluct.android.compose.components.cards.task_entry

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import work.racka.reluct.android.compose.components.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun RoundCheckbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit = { },
) {

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .toggleable(value = isChecked, role = Role.Checkbox) { checked ->
                onCheckedChange(checked)
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isChecked,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Icon(
                modifier = modifier,
                imageVector = Icons.Rounded.CheckCircleOutline,
                contentDescription = stringResource(id = R.string.checkbox_checked)
            )
        }

        AnimatedVisibility(
            visible = !isChecked,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Icon(
                modifier = modifier,
                imageVector = Icons.Rounded.RadioButtonUnchecked,
                contentDescription = stringResource(id = R.string.checkbox_unchecked)
            )
        }
    }
}