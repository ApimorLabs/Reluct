package work.racka.reluct.android.compose.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.theme.Dimens

@Composable
fun OutlinedReluctButton(
    buttonText: String,
    icon: ImageVector?,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    buttonTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = LocalContentColor.current,
    shape: Shape = CircleShape,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onButtonClicked,
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp
        ),
        border = BorderStroke(1.0.dp, borderColor),
        modifier = modifier,
        enabled = enabled
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor
            )
            Spacer(
                modifier = Modifier
                    .width(Dimens.ExtraSmallPadding.size),
            )
        }
        Text(
            text = buttonText,
            modifier = Modifier
                .padding(
                    vertical = Dimens.SmallPadding.size
                ),
            color = contentColor,
            style = buttonTextStyle,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
