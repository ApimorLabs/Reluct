package work.racka.reluct.compose.common.components.cards.taskLabelEntry

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.cards.cardWithActions.ReluctDescriptionCard
import work.racka.reluct.compose.common.components.checkboxes.RoundCheckbox
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.textfields.texts.EntryDescription
import work.racka.reluct.compose.common.components.textfields.texts.EntryHeading
import work.racka.reluct.compose.common.components.util.getContentColor
import work.racka.reluct.compose.common.components.util.toColor

@Composable
fun TaskLabelEntry(
    label: TaskLabel,
    onEntryClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    entryMode: TaskLabelsEntryMode = TaskLabelsEntryMode.SelectLabels,
    isSelected: Boolean = false,
    onEdit: () -> Unit = {}
) {
    val labelColors by remember(label.colorHexString) {
        derivedStateOf {
            val color = label.colorHexString.toColor()
            val contentColor = color.getContentColor()
            color to contentColor
        }
    }

    val containerColor by animateColorAsState(
        targetValue = if (isSelected && entryMode == TaskLabelsEntryMode.SelectLabels) {
            labelColors.first
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected && entryMode == TaskLabelsEntryMode.SelectLabels) {
            labelColors.second
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )

    ReluctDescriptionCard(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        title = {
            EntryHeading(text = label.name, color = LocalContentColor.current)
        },
        description = {
            EntryDescription(
                text = label.description.ifBlank { stringResource(SharedRes.strings.no_description_text) },
                color = LocalContentColor.current
            )
        },
        onClick = onEntryClick,
        leftItems = {
            if (entryMode == TaskLabelsEntryMode.SelectLabels) {
                RoundCheckbox(
                    isChecked = isSelected,
                    onCheckedChange = {
                        onCheckedChange(it)
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = labelColors.first, shape = CircleShape)
                )
            }
        },
        rightItems = {
            if (entryMode == TaskLabelsEntryMode.SelectLabels) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = labelColors.first, shape = CircleShape)
                )
            } else {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }
            }
        }
    )
}
