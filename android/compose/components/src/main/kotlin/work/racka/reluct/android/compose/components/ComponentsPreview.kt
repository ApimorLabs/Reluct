package work.racka.reluct.android.compose.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import work.racka.reluct.android.compose.components.bottom_sheet.add_edit_task.AddEditTaskSheetPreview
import work.racka.reluct.android.compose.components.cards.task_entry.GroupedTasksPrev

@Composable
@Preview
fun ComponentsPreview() {
    Surface {
        GroupedTasksPrev()
    }
}

@Composable
@Preview
fun ComponentsPreview2() {
    Surface {
        AddEditTaskSheetPreview()
    }
}