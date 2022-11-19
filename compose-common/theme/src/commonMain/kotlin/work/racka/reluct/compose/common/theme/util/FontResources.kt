package work.racka.reluct.compose.common.theme.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

/**
 * Make sure you fonts are inside a folder called "font"
 */
@Composable
expect fun fontResources(
    font: String,
    weight: FontWeight,
    style: FontStyle
): Font
