package work.racka.reluct.compose.common.components.textfields.texts

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
class HighlightTextProps(
    val text: String,
    val url: String? = null,
    val color: Color
)
