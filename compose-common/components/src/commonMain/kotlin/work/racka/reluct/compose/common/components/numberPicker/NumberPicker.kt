package work.racka.reluct.compose.common.components.numberPicker

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.collections.immutable.toImmutableList

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: Iterable<Int>,
    modifier: Modifier = Modifier,
    label: (Int) -> String = {
        it.toString()
    },
    dividersColor: Color = MaterialTheme.colors.primary,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    ListItemPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        list = range.toImmutableList(),
        textStyle = textStyle
    )
}
