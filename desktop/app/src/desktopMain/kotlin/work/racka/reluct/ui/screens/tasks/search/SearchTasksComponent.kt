package work.racka.reluct.ui.screens.tasks.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import work.racka.reluct.ui.screens.ComposeRenderer

class SearchTasksComponent(
    componentContext: ComponentContext,
    private val onShowDetails: (taskId: String) -> Unit,
    private val onExit: () -> Unit
) : ComponentContext by componentContext, ComposeRenderer {

    @Composable
    override fun Render(modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Search Tasks")
        }
    }
}
