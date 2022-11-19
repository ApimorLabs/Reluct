package work.racka.reluct.android.screens.onboarding.pages

import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.onboarding.components.PermissionStatusCard
import work.racka.reluct.android.screens.util.BackPressHandler
import work.racka.reluct.android.screens.util.PermissionCheckHandler
import work.racka.reluct.android.screens.util.checkUsageAccessPermissions
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun UsageAccessPage(
    isGranted: Boolean,
    updatePermissionCheck: (isGranted: Boolean) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackPressHandler { goBack() } // Handle Back Presses

    val drawableSize = 300.dp
    val context = LocalContext.current

    val openDialog = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            updatePermissionCheck(checkUsageAccessPermissions(context))
        }
    )

    PermissionCheckHandler {
        if (!isGranted) {
            updatePermissionCheck(checkUsageAccessPermissions(context))
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            ListGroupHeadingHeader(
                text = stringResource(id = R.string.usage_access_text),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize),
                painter = painterResource(id = R.drawable.stats_pie_chart),
                contentDescription = null
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.usage_access_desc_text),
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
                    openDialog.value = true
                }
            }
        }
    }

    // Go To Usage Access Dialog
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = stringResource(R.string.open_settings_dialog_title))
            },
            text = {
                Text(text = stringResource(R.string.usage_permissions_rationale_dialog_text))
            },
            confirmButton = {
                ReluctButton(
                    buttonText = stringResource(R.string.ok),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        openDialog.value = false
                        launcher.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                    }
                )
            },
            dismissButton = {
                ReluctButton(
                    buttonText = stringResource(R.string.cancel),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onButtonClicked = { openDialog.value = false }
                )
            }
        )
    }
}
