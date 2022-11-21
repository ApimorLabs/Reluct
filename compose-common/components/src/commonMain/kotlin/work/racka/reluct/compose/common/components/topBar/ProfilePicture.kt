package work.racka.reluct.android.compose.components.topBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.images.rememberAsyncImage
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.theme.Shapes

@Composable
fun ProfilePicture(
    pictureUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    onPictureClicked: () -> Unit = { },
) {
    var imageLoading by remember {
        mutableStateOf(true)
    }

    val painter = pictureUrl?.let {
        rememberAsyncImage(
            url = it,
            onStartLoading = { imageLoading = true },
            onFinishLoading = { imageLoading = false }
        )
    }

    IconButton(
        modifier = modifier.size(size),
        onClick = onPictureClicked
    ) {
        if (painter == null) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(SharedRes.strings.profile_picture),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(size)
            )
        } else {
            Image(
                modifier = Modifier
                    .clip(Shapes.large)
                    .fillMaxSize(),
                painter = painter,
                contentDescription = stringResource(SharedRes.strings.profile_picture)
            )
        }
    }
}
