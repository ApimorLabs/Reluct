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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.ReluctAppTheme
import work.racka.reluct.android.compose.theme.Shapes

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
        rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(pictureUrl)
                .crossfade(true)
                .listener(
                    onStart = {
                        imageLoading = true
                    },
                    onSuccess = { _, _ ->
                        imageLoading = false
                    }
                )
                .build()
        )
    }

    IconButton(
        modifier = modifier.size(size),
        onClick = onPictureClicked
    ) {
        if (painter == null) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(id = R.string.profile_picture),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(size)
            )
        } else {
            Image(
                modifier = Modifier
                    .clip(Shapes.large)
                    .fillMaxSize(),
                painter = painter,
                contentDescription = stringResource(id = R.string.profile_picture)
            )
        }
    }
}

@Composable
@Preview
private fun ProfilePicturePreview() {
    ReluctAppTheme {
        ProfilePicture(pictureUrl = null)
    }
}
