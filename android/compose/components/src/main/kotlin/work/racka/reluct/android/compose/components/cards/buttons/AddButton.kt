package work.racka.reluct.android.compose.components.cards.buttons

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes

@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onButtonClicked: () -> Unit,
    expanded: Boolean = true,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = { onButtonClicked() },
            shape = Shapes.large,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = Dimens.MediumPadding.size)
                    .animateContentSize()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.add_icon)
                )
                if (expanded) {
                    Text(
                        modifier = modifier,
                        text = buttonText,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = LocalContentColor.current
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddButtonPrev() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
        ) {
            AddButton(
                buttonText = "New Task",
                onButtonClicked = { }
            )
            AddButton(
                buttonText = "New Task",
                onButtonClicked = { },
                expanded = false
            )
        }
    }
}