package work.racka.reluct.android.compose.components.bottomSheet.taskLabels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.android.compose.components.bottomSheet.TopSheetSection
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.taskLabelEntry.TaskLabelEntry
import work.racka.reluct.android.compose.components.cards.taskLabelEntry.TaskLabelsEntryMode
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.common.model.domain.tasks.TaskLabel
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.util.navigationBarsPadding
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnSelectTaskLabelsSheet(
    onClose: () -> Unit,
    availableLabels: ImmutableList<TaskLabel>,
    selectedLabels: ImmutableList<TaskLabel>,
    onModifyLabel: (TaskLabel?) -> Unit,
    onEditLabels: (isAdd: Boolean, label: TaskLabel) -> Unit,
    modifier: Modifier = Modifier,
    entryMode: TaskLabelsEntryMode = TaskLabelsEntryMode.SelectLabels,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
    ) {
        stickyHeader {
            TopSheetSection(
                sheetTitle = stringResource(SharedRes.strings.select_task_labels_text),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onCloseClicked = onClose
            )
        }

        if (availableLabels.isEmpty()) {
            item {
                LottieAnimationWithDescription(
                    lottieResource = SharedRes.files.no_task_animation,
                    imageSize = 200.dp,
                    description = stringResource(SharedRes.strings.no_saved_label_text)
                )
            }
        } else {
            items(availableLabels, key = { it.id }) { item ->
                val selected by remember(selectedLabels) {
                    derivedStateOf {
                        selectedLabels.any { it.id == item.id }
                    }
                }

                TaskLabelEntry(
                    entryMode = entryMode,
                    label = item,
                    isSelected = selected,
                    onEntryClick = { onEditLabels(!selected, item) },
                    onCheckedChange = { onEditLabels(it, item) },
                    onEdit = { onModifyLabel(item) }
                )
            }
        }

        item {
            ReluctButton(
                modifier = Modifier.fillMaxWidth(.7f),
                shape = Shapes.large,
                buttonText = stringResource(SharedRes.strings.new_task_label_text),
                icon = Icons.Rounded.Add,
                onButtonClicked = { onModifyLabel(null) }
            )
        }

        // Bottom Space
        item {
            Spacer(
                modifier = Modifier
                    .padding(bottom = Dimens.MediumPadding.size)
                    .navigationBarsPadding()
            )
        }
    }
}
