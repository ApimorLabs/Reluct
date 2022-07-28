package work.racka.reluct.android.screens.screentime.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import work.racka.reluct.android.compose.theme.Dimens

@Composable
internal fun AppNameEntry(
    modifier: Modifier = Modifier,
    appName: String,
    icon: Drawable,
    iconSize: Dp = 32.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier.size(iconSize),
            painter = rememberAsyncImagePainter(model = icon),
            contentDescription = appName
        )

        Text(
            modifier = Modifier.weight(1f),
            text = appName,
            style = MaterialTheme.typography.bodyLarge,
            color = LocalContentColor.current
        )
    }
}