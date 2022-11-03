package work.racka.reluct.common.features.screenTime.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import work.racka.reluct.common.features.screen_time.R
import work.racka.reluct.common.features.screenTime.statistics.AppScreenTimeStatsViewModel
import work.racka.reluct.common.features.screenTime.ui.overlay.AppLimitedOverlayView
import work.racka.reluct.common.features.screenTime.ui.overlay.LimitsOverlayParams
import work.racka.reluct.common.features.screenTime.ui.overlay.OverlayLifecycleOwner

internal class ScreenTimeLimitService : Service(), KoinComponent {

    private val overlayLifecycleOwner = OverlayLifecycleOwner()

    private var scope: CoroutineScope? = null
    private var observeLimitsJob: Job? = null

    /**
     * ViewModel gets populated when [overlayWindow] is called
     */
    private var viewModel: AppScreenTimeStatsViewModel? = null

    private val screenTimeServices: ScreenTimeServices by inject()

    private var windowManager: WindowManager? = null

    private var overlayView: ComposeView? = null
    private var overlaidAppPackageName = ""
    private var goneHome = false

    override fun onCreate() {
        scope?.cancel()
        scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        initializeService()
        super.onCreate()
    }

    override fun onDestroy() {
        android.view.KeyEvent.KEYCODE_HOME
        scope?.cancel()?.also { scope = null }
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

    private suspend fun overlayWindow(packageName: String) {
        withContext(Dispatchers.Main) {
            val canDrawOverlays = Settings.canDrawOverlays(applicationContext)
            if (canDrawOverlays && overlayView == null) {
                println("Setting Window: $packageName")
                viewModel = get { parametersOf(packageName) }
                overlaidAppPackageName = packageName
                val layoutParams = LimitsOverlayParams.getParams()
                overlayView = viewModel?.let { vm ->
                    AppLimitedOverlayView(
                        applicationContext,
                        viewModel = vm,
                        exit = {
                            goHome()
                            removeOverlayView()
                        }
                    ).getView()
                }
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

    private fun removeOverlayView() {
        overlayView?.let { view ->
            viewModel?.destroy() // Destroy ViewModel
            viewModel = null
            view.disposeComposition() // Remove Composition
            windowManager?.removeView(view)
            overlaidAppPackageName = ""
            overlayView = null
        }
    }

    private suspend fun manageLimits() {
        screenTimeServices.observeCurrentAppBlocking().collectLatest { blockState ->
            // Delay Checking Limits if a home action was initiated
            delayAfterHome()

            when (blockState) {
                is ScreenTimeServices.BlockState.Allowed -> {
                    // Remove Overlay if packages don't match
                    if (overlaidAppPackageName != blockState.appPackageName &&
                        overlaidAppPackageName.isNotBlank()
                    ) {
                        withContext(Dispatchers.Main) { removeOverlayView() }
                    }
                }
                else -> {
                    if (!goneHome) overlayWindow(blockState.appPackageName)
                }
            }
        }
    }

    private fun goHome() {
        goneHome = true
        try {
            Intent(Intent.ACTION_MAIN).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                addCategory(Intent.CATEGORY_HOME)
            }.also { intent ->
                startActivity(intent)
            }
        } catch (e: Exception) {
            // Failed To Send Home
            Log.d(TAG, "Failed To Go Home: ${e.message}")
        }
    }

    /**
     * We need to delay execution after [goHome] was called
     * This will help prevent the [overlayView] from being created again when the
     * home action delays.
     */
    private suspend fun delayAfterHome() {
        if (goneHome) {
            goneHome = false
            delay(1000)
        }
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
