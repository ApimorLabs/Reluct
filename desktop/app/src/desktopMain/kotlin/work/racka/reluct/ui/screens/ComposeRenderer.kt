package work.racka.reluct.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ComposeRenderer {
    @Composable
    fun Render(modifier: Modifier = Modifier)
}
