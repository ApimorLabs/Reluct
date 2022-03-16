package work.racka.reluct.ui.components

import android.app.usage.UsageStats
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.data.local.usagestats.AppUsageInfo
import work.racka.reluct.utils.Utils

@Composable
fun UsageStat(
    packageName: String,
    stats: UsageStats
) {
    Column(
        modifier = Modifier.padding(Dimens.MediumPadding.size),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val timeInForeground by remember {
            mutableStateOf(
                Utils.getFormattedTime(stats.totalTimeInForeground)
            )
        }

        val lastTimeUsed by remember {
            mutableStateOf(
                Utils.getFormattedTime(stats.lastTimeUsed)
            )
        }

        Text(text = "Package Name: $packageName")
        Text(text = "Time used in foreground: $timeInForeground")
        Text(text = "First Timestamp: ${stats.firstTimeStamp}")
        Text(text = "Last Time Used: $lastTimeUsed")
    }
}

@Composable
fun UsageStat(
    appInfo: AppUsageInfo
) {
    Column(
        modifier = Modifier.padding(Dimens.MediumPadding.size),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val timeInForeground by remember(appInfo) {
            mutableStateOf(
                Utils.getFormattedTime(appInfo.timeInForeground)
            )
        }

        val icon by remember {
            mutableStateOf(
                appInfo.appIcon
            )
        }

        appInfo.appIcon?.let {
            Image(
                it.toBitmap().asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .size(Dimens.LargePadding.size)
            )
        }

        Text(text = "Package Name: ${appInfo.packageName}")
        Text(text = "Time used in foreground: $timeInForeground")
    }
}