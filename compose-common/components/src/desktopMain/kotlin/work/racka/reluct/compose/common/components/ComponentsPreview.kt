package work.racka.reluct.android.compose.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import work.racka.reluct.android.compose.components.cards.taskEntry.GroupedTasksPrev

@Composable
@Preview
fun ComponentsPreview() {
    Surface {
        GroupedTasksPrev()
    }
}
