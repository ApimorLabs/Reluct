package work.racka.reluct.ui.main

import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import work.racka.reluct.data.local.usagestats.AppUsageInfo
import work.racka.reluct.data.local.usagestats.UsageDataManager
import work.racka.reluct.ui.components.Greeting
import work.racka.reluct.ui.components.PermCheck
import work.racka.reluct.ui.components.UsageStat
import work.racka.reluct.ui.theme.ComposeAndroidTemplateTheme
import work.racka.reluct.ui.theme.Dimens
import work.racka.reluct.utils.Util
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var usageStatsManager: UsageStatsManager

    @Inject
    lateinit var usageDataManager: UsageDataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable support for Splash Screen API for
        // proper Android 12+ support
        installSplashScreen()

        setContent {
            ComposeAndroidTemplateTheme {
                // A surface container using the 'background' color from the theme
                val isGranted by remember {
                    mutableStateOf(
                        Util.checkUsageAccessPermissions(this)
                    )
                }

                val usageStats by remember {
                    val stats = usageDataManager.getWeeklyUsage(0)
                    mutableStateOf(Util.sortByHighestForegroundTime(stats.first().appsData))
                }

                Surface(color = MaterialTheme.colors.background) {
                    LazyColumn(
                        state = rememberLazyListState(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Greeting("Compose")
                            Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))
                            PermCheck(isGranted = isGranted)
                            Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))
                            Button(onClick = {
                                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                                startActivity(intent)
                            }) {
                                Text(text = "Request")
                            }
                        }

                        items(usageStats) {
                            UsageStat(
                                appInfo = it
                            )
                        }

//                        usageStats?.let { usageStats ->
//                            item {
//                                usageStats.forEach { (key, value) ->
//                                    UsageStat(
//                                        packageName = key,
//                                        stats = value
//                                    )
//                                }
//                            }
//                        }
                    }
                }
            }
        }
    }

    private fun queryUsageStats(): List<UsageStats>? {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis
        val end = System.currentTimeMillis()
        return usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            start,
            end
        )
    }

    private fun queryAggregatedStats(): Map<String, UsageStats>? {
        val calendar = Calendar.getInstance()
        //calendar.add(Calendar.DAY_OF_MONTH, -1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val start = calendar.timeInMillis
        val end = System.currentTimeMillis()
        return usageStatsManager.queryAndAggregateUsageStats(
            start,
            end
        )
    }


    private fun getUsageStatistics(): List<AppUsageInfo> {
        var currentEvent: UsageEvents.Event? = null
        val allEvents = mutableListOf<UsageEvents.Event>()
        val map = hashMapOf<String, AppUsageInfo>()

        val startTime: Long
        val endTime: Long

        if (false) {
            startTime = startTime(1, 0)
            endTime = endTime(1, 0)
        } else {
            startTime = startTime(8, 1)
            endTime = endTime(8,1)
        }

        val usageEvents = usageStatsManager.queryEvents(startTime, endTime)

        while (usageEvents.hasNextEvent()) {
            currentEvent = UsageEvents.Event()
            usageEvents.getNextEvent(currentEvent)
            if (currentEvent.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
                currentEvent.eventType == UsageEvents.Event.ACTIVITY_PAUSED
            ) {
                allEvents.add(currentEvent)
                val key = currentEvent.packageName
                // taking it into a collection to access by package name
                if (map[key] == null) {
                    map[key] = AppUsageInfo(
                        key,
                        Util.getAppIcon(key, this)
                    )
                }
            }
        }

        for (i in 0 until allEvents.size - 1) {
            val e0 = allEvents[i]
            val e1 = allEvents[i + 1]
            //for UsageTime of apps in time range
            if (e0.eventType == 1 && e1.eventType == 2 && e0.className == e1.className) {
                val diff = e1.timeStamp - e0.timeStamp
                //phoneUsageToday += diff //global Long var for total usagetime in the timerange
                map[e0.packageName]!!.timeInForeground += diff
            }
        }

        return map.values.toList()
    }

    private fun startTime(day: Int, offsetWeekBy: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.WEEK_OF_YEAR, -offsetWeekBy)
            set(Calendar.DAY_OF_WEEK, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            Timber.d("Day Start: ${calendar.get(Calendar.DATE)}")
        }
        return calendar.timeInMillis
    }

    private fun endTime(day: Int, offsetWeekBy: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.apply {
            add(Calendar.WEEK_OF_YEAR, -offsetWeekBy)
            set(Calendar.DAY_OF_WEEK, day)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
            Timber.d("Day End: ${calendar.get(Calendar.DAY_OF_WEEK)}")
        }
        return calendar.timeInMillis
    }
}
