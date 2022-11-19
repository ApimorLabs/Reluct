package work.racka.reluct.android.compose.components.bottomSheet.taskLabels

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.cards.taskLabelEntry.TaskLabelPill
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskLabelsSelectCard(
    labels: ImmutableList<TaskLabel>,
    onEditLabels: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    cardHeight: Dp = 48.dp
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onEditLabels
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            LazyRow(
                modifier = Modifier
                    .height(cardHeight)
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
            ) {
                if (labels.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(R.string.no_selected_labels_text),
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalContentColor.current,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                } else {
                    items(labels) { label ->
                        TaskLabelPill(name = label.name, colorHex = label.colorHexString)
                    }
                }
            }

            Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
        }
    }
}
