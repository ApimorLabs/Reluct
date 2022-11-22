package work.racka.reluct.android.screens.onboarding.pages

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.onboarding.components.PermissionStatusCard
import work.racka.reluct.android.screens.util.BackPressHandler
import work.racka.reluct.android.screens.util.PermissionCheckHandler
import work.racka.reluct.android.screens.util.areNotificationsEnabled
import work.racka.reluct.android.screens.util.openAppNotificationSettings
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationsPage(
    isGranted: Boolean,
    updatePermissionCheck: (isGranted: Boolean) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackPressHandler { goBack() } // Handle Back Presses

    val drawableSize = 300.dp
    val context = LocalContext.current
    var permissionTries by remember { mutableStateOf(1) }

    PermissionCheckHandler {
        if (!isGranted) {
            updatePermissionCheck(context.areNotificationsEnabled())
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            updatePermissionCheck(granted)
            if (granted.not()) {
                permissionTries++
                Toast.makeText(context, "Retry requesting the Permission", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(id = R.string.notifications_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = painterResource(SharedRes.assets.push_notifications),
                contentDescription = null
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.notifications_desc_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            PermissionStatusCard(
                modifier = Modifier.padding(vertical = Dimens.MediumPadding.size),
                isGranted = isGranted
            ) {
                if (!isGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (permissionTries <= 2) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            // Open settings when user tries more than twice
                            context.openAppNotificationSettings()
                        }
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // Open Notification Settings directly for older versions
                        context.openAppNotificationSettings()
                    }
                }
            }
        }
    }
}
