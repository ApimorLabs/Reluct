package work.racka.reluct.android.compose.components.cards.app_usage_entry

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import coil.compose.rememberImagePainter
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUsageEntry(
    modifier: Modifier = Modifier,
    appUsageInfo: AppUsageInfo,
    onEntryClick: () -> Unit,
    onTimeSettingsClick: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = { onEntryClick() },
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement
                .spacedBy(Dimens.SmallPadding.size),
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(Dimens.LargePadding.size),
                painter = rememberImagePainter(data = appUsageInfo.appIcon.icon),
                contentDescription = appUsageInfo.appName
            )

            AppNameAndTimeText(
                modifier = Modifier.weight(1f),
                appName = appUsageInfo.appName,
                timeText = appUsageInfo.formattedTimeInForeground
            )

            IconButton(onClick = onTimeSettingsClick) {
                Icon(
                    imageVector = Icons.Rounded.HourglassBottom,
                    contentDescription = stringResource(R.string.time_limit_settings_icon)
                )
            }
        }
    }
}

/*@Composable
private fun AppUsageTextAndTimerButton(
    modifier: Modifier = Modifier,
    appName: String,
    timeText: String,
    onTimeSettingsClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        
    }
}*/

@Composable
private fun AppNameAndTimeText(
    modifier: Modifier = Modifier,
    appName: String,
    timeText: String
) {
    Column(
        modifier = modifier
    ) {
        AppNameHeading(text = appName)
        Spacer(modifier = Modifier.width(Dimens.SmallPadding.size))
        TimeInfoText(text = timeText)
    }
}