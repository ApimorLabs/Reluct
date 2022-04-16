package work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import work.racka.reluct.common.model.domain.tasks.EditTask

@Composable
fun AddEditTaskSheet(
    modifier: Modifier,
    editTask: EditTask?,
    onSave: (EditTask) -> Unit = { },
) {
    val task = remember {
        if (editTask == null) {

        }
        mutableStateOf(editTask)
    }
}