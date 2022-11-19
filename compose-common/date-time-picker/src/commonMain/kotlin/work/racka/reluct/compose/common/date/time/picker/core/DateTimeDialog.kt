package work.racka.reluct.compose.common.date.time.picker.core

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
internal expect fun DateTimeDialog(
    onCloseDialog: () -> Unit,
    onPositiveButtonClicked: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    properties: DateTimeDialogProperties = DateTimeDialogProperties(),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RoundedCornerShape(10.dp),
    positiveButtonText: String? = "OK",
    negativeButtonText: String? = "CANCEL",
    content: @Composable () -> Unit = {},
)

@Composable
internal fun DialogContentHolder(
    positiveButtonClicked: () -> Unit,
    negativeButtonClicked: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RoundedCornerShape(10.dp),
    positiveButtonText: String? = "OK",
    negativeButtonText: String? = "CANCEL",
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .wrapContentHeight()
            then modifier,
        shape = shape,
        color = containerColor
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            contentPadding = PaddingValues(16.dp)
        ) {
            item { content() }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    if (negativeButtonText != null) {
                        val buttonText = negativeButtonText.uppercase(Locale.getDefault())
                        Button(
                            onClick = negativeButtonClicked
                        ) {
                            Text(buttonText)
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                    if (positiveButtonText != null) {
                        val buttonText = positiveButtonText.uppercase(Locale.getDefault())
                        Button(
                            onClick = positiveButtonClicked
                        ) {
                            Text(buttonText)
                        }
                    }
                }
            }
        }
    }
}

@Stable
data class DateTimeDialogProperties(
    val desktopWindowSize: DpSize = DpSize(400.dp, 500.dp),
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
    val shape: Shape = RoundedCornerShape(10.dp)
)
