package work.racka.reluct.ui.screens.screenTime.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import work.racka.reluct.common.model.domain.core.Icon
import work.racka.reluct.compose.common.components.images.painterResource
import work.racka.reluct.compose.common.theme.Dimens

@Composable
internal fun AppNameEntry(
    appName: String,
    icon: Icon,
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    contentPadding: Dp = Dimens.SmallPadding.size,
    iconSize: Dp = 32.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            Image(
                modifier = Modifier.size(iconSize),
                painter = painterResource(icon),
                contentDescription = appName
            )

            Text(
                modifier = Modifier.weight(1f),
                text = appName,
                style = textStyle,
                color = contentColor
            )

            actions()
        }
    }
}
