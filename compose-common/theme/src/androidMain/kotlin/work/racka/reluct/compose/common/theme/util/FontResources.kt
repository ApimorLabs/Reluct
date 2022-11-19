package work.racka.reluct.compose.common.theme.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

@SuppressLint("DiscouragedApi")
@Composable
actual fun fontResources(
    font: String,
    weight: FontWeight,
    style: FontStyle
): Font {
    val context = LocalContext.current
    val name = font.substringBefore(".")
    val fontRes = context.resources.getIdentifier(name, "font", context.packageName)
    return Font(fontRes, weight, style)
}
