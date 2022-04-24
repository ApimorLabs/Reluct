package work.racka.reluct.android.compose.components.cards.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.android.compose.components.checkboxes.RoundCheckbox
import work.racka.reluct.android.compose.theme.Dimens

@Composable
fun EntryWithCheckbox(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onCheckedChanged: (Boolean) -> Unit,
) {

    val isChecked = remember {
        mutableStateOf(false)
    }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = LocalContentColor.current,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(Dimens.SmallPadding.size))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current
                    .copy(alpha = ContentAlpha.medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))

        RoundCheckbox(
            isChecked = isChecked.value,
            onCheckedChange = { checked ->
                isChecked.value = checked
                onCheckedChanged(isChecked.value)
            }
        )
    }
}