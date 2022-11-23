package work.racka.reluct.ui.screens.goals.inactive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import work.racka.reluct.ui.screens.ComposeRenderer

class InactiveGoalsComponent(
    componentContext: ComponentContext,
    private val onShowDetails: (goalId: String) -> Unit,
    private val onAddGoal: (defaultGoalIndex: Int?) -> Unit,
    private val onGoBack: () -> Unit
) : ComponentContext by componentContext, ComposeRenderer {

    @Composable
    override fun Render(modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Inactive Goals")
        }
    }
}
