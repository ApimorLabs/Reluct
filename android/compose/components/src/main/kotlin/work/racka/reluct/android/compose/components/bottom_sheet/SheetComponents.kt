package work.racka.reluct.android.compose.components.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens

@Composable
internal fun TopSheetSection(
    modifier: Modifier = Modifier,
    sheetTitle: String,
    rightButtonIcon: ImageVector,
    rightButtonContentDescription: String?,
    onCloseClicked: () -> Unit = { },
    onRightButtonClicked: () -> Unit = { },
    closeButtonVisible: Boolean = true,
    rightButtonVisible: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(height = 5.dp, width = 32.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground
                        .copy(alpha = .1f),
                    shape = CircleShape
                )
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = onCloseClicked,
                enabled = closeButtonVisible
            ) {
                if (closeButtonVisible) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(id = R.string.close_icon),
                        tint = LocalContentColor.current
                    )
                }
            }

            Text(
                text = sheetTitle,
                style = MaterialTheme.typography.titleLarge,
                color = LocalContentColor.current,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )


            IconButton(
                onClick = onRightButtonClicked,
                enabled = rightButtonVisible
            ) {
                if (rightButtonVisible) {
                    Icon(
                        imageVector = rightButtonIcon,
                        contentDescription = rightButtonContentDescription,
                        tint = LocalContentColor.current
                    )
                }
            }
        }
    }
}

@Composable
fun SheetButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    buttonTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    icon: ImageVector?,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = LocalContentColor.current,
    onButtonClicked: () -> Unit,
    enabled: Boolean = true,
) {

    Button(
        onClick = onButtonClicked,
        shape = CircleShape,
        elevation = ButtonDefaults
            .buttonElevation(
                defaultElevation = 0.dp
            ),
        colors = ButtonDefaults
            .buttonColors(
                containerColor = buttonColor,
                contentColor = contentColor
            ),
        modifier = modifier,
        enabled = enabled
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = buttonText,
            modifier = Modifier
                .padding(
                    horizontal = Dimens.ExtraSmallPadding.size,
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