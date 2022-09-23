package work.racka.reluct.android.screens.onboarding.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.onboarding.components.PermissionStatusCard
import work.racka.reluct.android.screens.util.PermissionCheckHandler

@Composable
internal fun AlarmsAndRemindersPage(
    modifier: Modifier = Modifier,
    isGranted: Boolean,
    updatePermissionCheck: (isGranted: Boolean) -> Unit
) {

    val drawableSize = 400.dp

    PermissionCheckHandler {
        if (!isGranted) {
            updatePermissionCheck(true) // TODO: DO Actual Check Here
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimens.LargePadding.size) then modifier,
        verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(id = R.string.alarms_and_reminders_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge
                    .copy(fontSize = 40.sp)
            )
        }

        item {
            Image(
                modifier = Modifier
                    .size(drawableSize)
                    .padding(Dimens.MediumPadding.size),
                painter = painterResource(id = R.drawable.alarms_and_reminders),
                contentDescription = null
            )
        }

        item {
            Text(
                text = stringResource(id = R.string.alarms_and_reminders_desc_text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            PermissionStatusCard(isGranted = isGranted) {
                if (!isGranted) {
                    // TODO: Request Permission
                }
            }
        }
    }
}