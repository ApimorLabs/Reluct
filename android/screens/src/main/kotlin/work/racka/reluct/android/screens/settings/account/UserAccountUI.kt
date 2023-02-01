package work.racka.reluct.android.screens.settings.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.Pin
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.settings.states.UserAccountState
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.OutlinedReluctButton
import work.racka.reluct.compose.common.components.buttons.ReluctButton
import work.racka.reluct.compose.common.components.images.rememberAsyncImage
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.textfields.texts.EntryDescription
import work.racka.reluct.compose.common.components.textfields.texts.EntryHeading
import work.racka.reluct.compose.common.theme.Dimens

@Composable
internal fun UserAccountUI(
    accountState: UserAccountState.Account,
    onRefreshUser: () -> Unit,
    onRequestPasswordReset: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val drawableSize = 160.dp
    val buttonRatio = .9f // 90%
    val pfpLoading = remember { mutableStateOf(false) }

    val profileImage = accountState.user.profilePicUrl?.let { url ->
        rememberAsyncImage(
            url = url,
            onStartLoading = { pfpLoading.value = true },
            onFinishLoading = { pfpLoading.value = false }
        )
    } ?: painterResource(resource = SharedRes.assets.profile_pic)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        contentPadding = PaddingValues(vertical = Dimens.MediumPadding.size)
    ) {
        /*stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(id = R.string.manage_acc_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 35.sp)
            )
        }*/

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = profileImage,
                contentDescription = null
            )
        }

        // Username
        item {
            EntryHeading(
                modifier = Modifier.fillMaxWidth(buttonRatio),
                text = stringResource(id = R.string.display_name_text)
            )
            EntryDescription(
                modifier = Modifier.fillMaxWidth(buttonRatio),
                text = accountState.user.displayName,
                color = LocalContentColor.current.copy(.8f)
            )
        }

        // Email
        item {
            Row(
                modifier = Modifier.fillMaxWidth(buttonRatio),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    EntryHeading(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.email_text)
                    )
                    EntryDescription(
                        modifier = Modifier.fillMaxWidth(),
                        text = accountState.user.email,
                        color = LocalContentColor.current.copy(.8f)
                    )
                }
                Icon(
                    imageVector = if (accountState.user.isEmailVerified) {
                        Icons.Filled.CheckCircle
                    } else {
                        Icons.Filled.Warning
                    },
                    contentDescription = stringResource(
                        id = if (accountState.user.isEmailVerified) {
                            R.string.verify_email_done_text
                        } else {
                            R.string.verify_email_done_text
                        }
                    ),
                    tint = if (accountState.user.isEmailVerified) Color.Green else Color.Yellow
                )
            }
        }

        // Refresh Button
        item {
            ReluctButton(
                buttonText = stringResource(R.string.refresh_acc_details_text),
                icon = Icons.Rounded.Refresh,
                onButtonClicked = onRefreshUser,
                modifier = Modifier.fillMaxSize(buttonRatio),
                enabled = !accountState.isUpdating
            )
        }

        // Request Password Reset Button
        item {
            ReluctButton(
                buttonText = stringResource(R.string.password_reset_text),
                icon = Icons.Rounded.Pin,
                onButtonClicked = onRequestPasswordReset,
                modifier = Modifier.fillMaxSize(buttonRatio),
                enabled = !accountState.isUpdating,
                showLoading = accountState.isUpdating
            )
        }

        // Logout Button
        item {
            OutlinedReluctButton(
                buttonText = stringResource(R.string.logout_text),
                icon = Icons.Rounded.Logout,
                onButtonClicked = onLogout,
                modifier = Modifier.fillMaxSize(buttonRatio),
                enabled = !accountState.isUpdating
            )
        }
    }
}
