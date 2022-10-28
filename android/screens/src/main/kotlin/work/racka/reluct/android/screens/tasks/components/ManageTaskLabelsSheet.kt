package work.racka.reluct.android.screens.tasks.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.bottom_sheet.task_labels.AddEditTaskLabelSheet
import work.racka.reluct.android.compose.components.bottom_sheet.task_labels.LazyColumnSelectTaskLabelsSheet
import work.racka.reluct.android.compose.components.cards.task_label_entry.TaskLabelsEntryMode
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.model.domain.tasks.TaskLabel

@Stable
internal data class CurrentTaskLabels(
    val availableLabels: List<TaskLabel>,
    val selectedLabels: List<TaskLabel>,
    val onUpdateSelectedLabels: (List<TaskLabel>) -> Unit
)

@Stable
internal sealed class TaskLabelsPage {
    class ModifyLabel(val label: TaskLabel? = null) : TaskLabelsPage()
    object ShowLabels : TaskLabelsPage()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun ManageTaskLabelsSheet(
    modifier: Modifier = Modifier,
    tonalElevation: Dp = 6.dp,
    shape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    entryMode: TaskLabelsEntryMode = TaskLabelsEntryMode.SelectLabels,
    labelsState: CurrentTaskLabels,
    startPage: TaskLabelsPage = TaskLabelsPage.ShowLabels,
    onSaveLabel: (TaskLabel) -> Unit,
    onDeleteLabel: (TaskLabel) -> Unit,
    onClose: () -> Unit
) {
    val page = remember(startPage) {
        mutableStateOf(startPage)
    }
    Surface(
        modifier = modifier,
        tonalElevation = tonalElevation,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = shape
    ) {
        AnimatedContent(
            targetState = page.value,
            modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
            contentAlignment = Alignment.Center
        ) { targetState ->
            when (targetState) {
                is TaskLabelsPage.ShowLabels -> {
                    LazyColumnSelectTaskLabelsSheet(
                        entryMode = entryMode,
                        onClose = onClose,
                        availableLabels = labelsState.availableLabels,
                        selectedLabels = labelsState.selectedLabels,
                        onAddNewLabel = { page.value = TaskLabelsPage.ModifyLabel(null) },
                        onEditLabels = { isAdd: Boolean, label: TaskLabel ->
                            labelsState.selectedLabels.toMutableList().apply {
                                if (isAdd) add(label)
                                else remove(label)
                            }.also { newList ->
                                labelsState.onUpdateSelectedLabels(newList.toList())
                            }
                        }
                    )
                }

                is TaskLabelsPage.ModifyLabel -> {
                    AddEditTaskLabelSheet(
                        onClose = { page.value = TaskLabelsPage.ShowLabels },
                        initialLabel = targetState.label,
                        onSaveLabel = onSaveLabel,
                        onDeleteLabel = onDeleteLabel
                    )
                }
            }
        }
    }
}