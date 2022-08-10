package work.racka.reluct.common.features.screen_time.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import work.racka.reluct.common.core_navigation.compose_destinations.screentime.AppScreenTimeStatsDestination
import work.racka.reluct.common.data.usecases.app_usage.GetDailyAppUsageInfo
import work.racka.reluct.common.data.usecases.limits.GetAppLimits
import work.racka.reluct.common.data.usecases.limits.ManageFocusMode
import work.racka.reluct.common.data.usecases.limits.ModifyAppLimits
import work.racka.reluct.common.features.screen_time.R
import work.racka.reluct.common.features.screen_time.ui.overlay.AppLimitedOverlayView
import work.racka.reluct.common.features.screen_time.ui.overlay.LimitsOverlayParams
import work.racka.reluct.common.features.screen_time.ui.overlay.OverlayLifecycleOwner
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats

internal class ScreenTimeLimitService : Service(), KoinComponent {

    private val overlayLifecycleOwner = OverlayLifecycleOwner()

    private var scope: CoroutineScope? = null
    private var observeLimitsJob: Job? = null

    private val screenTimeServices: ScreenTimeServices by inject()
    private val getDailyAppUsageInfo: GetDailyAppUsageInfo by inject()
    private val getAppLimits: GetAppLimits by inject()
    private val manageFocusMode: ManageFocusMode by inject()
    private val modifyAppLimits: ModifyAppLimits by inject()

    private var windowManager: WindowManager? = null

    private var overlayView: ComposeView? = null
    private var overlaidAppPackageName = ""

    override fun onCreate() {
        scope?.cancel()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        initializeService()
        super.onCreate()
    }

    override fun onDestroy() {
        scope?.cancel()
        screenTimeServices.startLimitsService()
        overlayLifecycleOwner.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        overlayLifecycleOwner.onResume()
        observeLimitsJob?.cancel()
        observeLimitsJob = scope?.launch {
            manageLimits()
        }
        return START_STICKY
    }

    private fun initializeService() {
        val notification = ScreenTimeServiceNotification
            .createNotification(
                context = applicationContext,
                title = getString(R.string.screen_time_service_default_title),
                content = getString(R.string.screen_time_service_default_content),
                onNotificationClick = { null }
            )
        startForeground(ScreenTimeServiceNotification.NOTIFICATION_ID, notification)
        overlayLifecycleOwner.onCreate()
        removeOverlayView()
        windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private fun removeOverlayView() {
        Log.d(TAG, "Removing View")
        overlayView?.let { view ->
            view.disposeComposition() // Remove Composition
            windowManager?.removeView(view)
            overlaidAppPackageName = ""
            overlayView = null
        }
    }

    private suspend fun overlayWindow(packageName: String) {
        withContext(Dispatchers.Main) {
            val canDrawOverlays = Settings.canDrawOverlays(applicationContext)
            if (canDrawOverlays && overlayView == null) {
                overlaidAppPackageName = packageName
                val layoutParams = LimitsOverlayParams.getParams()
                overlayView = AppLimitedOverlayView(
                    applicationContext,
                    packageName = packageName,
                    close = {
                        Log.d(TAG, "Close Overlay")
                        removeOverlayView()
                    }
                ).getView()
                overlayLifecycleOwner.attachToView(overlayView)
                overlayView?.let { windowManager?.addView(it, layoutParams) }
            } else if (!canDrawOverlays) {
                // Show Overlay Permission Notification
                ScreenTimeServiceNotification.overlayPermissionNotification(applicationContext)
                    .show()
            } else if (overlaidAppPackageName != packageName) {
                // Remove the Overlay
                removeOverlayView()
            }
            Unit // VOID Return value for withContext block!
        }
    }

    private suspend fun manageLimits() {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        val currentStats = ScreenTimeDataProviders
            .getCurrentAppStats(getDailyAppUsageInfo, applicationContext)
        currentStats.collectLatest { stats ->
            // Update Our Notification
            val notification = appStatsNotification(applicationContext, stats)
            notificationManager.notify(ScreenTimeServiceNotification.NOTIFICATION_ID, notification)

            val currentDuration = stats.appUsageInfo.timeInForeground
            val isFocusModeOn = manageFocusMode.isFocusModeOn.first()
            val appLimits = getAppLimits.getAppSync(stats.appUsageInfo.packageName)
            val appPastLimit = (currentDuration >= appLimits.timeLimit && appLimits.timeLimit != 0L)

            // Remove Overlay if packages don't match
            if (overlaidAppPackageName != appLimits.appInfo.packageName
                && overlaidAppPackageName.isNotBlank()
            ) {
                withContext(Dispatchers.Main) { removeOverlayView() }
            }

            // Check if the app doesn't violate limits
            if (!appLimits.overridden) {
                if (isFocusModeOn && appLimits.isADistractingAp) {
                    overlayWindow(appLimits.appInfo.packageName)
                } else if (appLimits.isPaused) {
                    overlayWindow(appLimits.appInfo.packageName)
                } else if (appPastLimit) {
                    modifyAppLimits.pauseApp(stats.appUsageInfo.packageName, isPaused = true)
                    overlayWindow(appLimits.appInfo.packageName)
                }
            } else {
                Log.d(TAG, "App Has Been Overridden it's limits!")
            }
        }
    }

    private fun appStatsNotification(context: Context, app: AppUsageStats): Notification {
        val uriString = AppScreenTimeStatsDestination
            .appScreenTimeDeepLink(app.appUsageInfo.packageName)
        val intent = Intent(
            Intent.ACTION_VIEW,
            uriString.toUri()
        )
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return ScreenTimeServiceNotification
            .createNotification(
                context = applicationContext,
                title = getString(R.string.current_app_arg, app.appUsageInfo.appName),
                content = getString(
                    R.string.current_app_time_arg,
                    app.appUsageInfo.formattedTimeInForeground
                ),
                onNotificationClick = { pendingIntent }
            )
    }

    // Do Not Bind this service
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        overlayLifecycleOwner.onResume()
    }

    companion object {
        private const val TAG = "ScreenTimeLimitService"
    }
}