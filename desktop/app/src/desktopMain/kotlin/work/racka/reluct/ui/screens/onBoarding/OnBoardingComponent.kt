package work.racka.reluct.ui.screens.onBoarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import work.racka.reluct.ui.screens.ComposeRenderer

class OnBoardingComponent(
    componentContext: ComponentContext,
    private val onOpenDashboard: () -> Unit
) : ComponentContext by componentContext, ComposeRenderer {

    @Composable
    override fun Render(modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "On Boarding")
                Button(
                    onClick = onOpenDashboard,
                    content = { Text("Continue") }
                )
            }
        }
    }
}
