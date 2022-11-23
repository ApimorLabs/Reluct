package work.racka.reluct.ui.screens.screenTime.appStats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import work.racka.reluct.ui.screens.ComposeRenderer

class AppScreenTimeComponent(
    componentContext: ComponentContext,
    val isMultipane: Value<Boolean>
) : ComponentContext by componentContext, ComposeRenderer {

    @Composable
    override fun Render(modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "App Screen Time Component")
        }
    }
}
