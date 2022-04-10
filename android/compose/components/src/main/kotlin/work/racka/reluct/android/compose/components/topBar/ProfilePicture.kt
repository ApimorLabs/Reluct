package work.racka.reluct.android.compose.components.topBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.ReluctAppTheme

@Composable
fun ProfilePicture(
    modifier: Modifier = Modifier,
    pictureUrl: String?,
    onPictureClicked: () -> Unit = { }
) {

    var imageLoading by remember {
        mutableStateOf(true)
    }

    val painter = pictureUrl?.let {
        rememberImagePainter(
            data = pictureUrl,
            builder = {
                crossfade(true)
                listener(
                    onStart = {
                        imageLoading = true
                    },
                    onSuccess = { _, _ ->
                        imageLoading = false
                    }
                )
            }
        )
    }


    IconButton(
        modifier = modifier,
        onClick = onPictureClicked
    ) {
        if (painter == null) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = stringResource(id = R.string.profile_picture),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(Dimens.LargePadding.size)
            )
        } else {
            Image(
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