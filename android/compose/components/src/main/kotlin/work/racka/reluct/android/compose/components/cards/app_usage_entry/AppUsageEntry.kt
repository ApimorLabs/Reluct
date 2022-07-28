package work.racka.reluct.android.compose.components.cards.app_usage_entry

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUsageEntry(
    modifier: Modifier = Modifier,
    playAnimation: Boolean = false,
    appUsageInfo: AppUsageInfo,
    onEntryClick: () -> Unit,
    onTimeSettingsClick: () -> Unit
) {

    //Scale animation
    val animatedProgress = remember(appUsageInfo) {
        Animatable(initialValue = 0.8f)
    }
    LaunchedEffect(key1 = appUsageInfo) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(300, easing = FastOutSlowInEasing)
        )
    }

    val animatedModifier = if (playAnimation) modifier
        .graphicsLayer(
            scaleX = animatedProgress.value,
            scaleY = animatedProgress.value
        ) else modifier

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = { onEntryClick() },
        modifier = animatedModifier
            .fillMaxWidth()
            .clip(Shapes.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement
                .spacedBy(Dimens.MediumPadding.size),
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(appUsageInfo.appIcon.icon).build()
                ),
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