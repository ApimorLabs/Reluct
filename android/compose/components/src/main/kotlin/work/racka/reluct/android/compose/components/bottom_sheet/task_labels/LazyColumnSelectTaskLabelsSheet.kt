package work.racka.reluct.android.compose.components.bottom_sheet.task_labels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.bottom_sheet.TopSheetSection
import work.racka.reluct.android.compose.components.cards.task_label_entry.TaskLabelEntry
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.model.domain.tasks.TaskLabel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyColumnSelectTaskLabelsSheet(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    availableLabels: List<TaskLabel>,
    selectedLabels: List<TaskLabel>,
    onEditLabels: (isAdd: Boolean, label: TaskLabel) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
    ) {
        stickyHeader {
            TopSheetSection(
                sheetTitle = stringResource(id = R.string.select_task_labels_text),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                onCloseClicked = onClose
            )
        }

        if (availableLabels.isEmpty()) {
            item {
                LottieAnimationWithDescription(
                    lottieResId = R.raw.no_task_animation,
                    imageSize = 200.dp,
                    description = stringResource(R.string.no_saved_label_text)
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
                    label = item,
                    isSelected = selected,
                    onEntryClick = { onEditLabels(!selected, item) },
                    onCheckedChange = { onEditLabels(it, item) }
                )
            }
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