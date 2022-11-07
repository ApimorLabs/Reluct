package work.racka.reluct

import android.app.Application
import androidx.work.WorkManager
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.logger.Level
import timber.log.Timber
import work.racka.reluct.common.di.intergration.KoinMain
import work.racka.reluct.common.features.screenTime.services.ScreenTimeServices
import work.racka.reluct.common.features.screenTime.work.ResumeAppsWork
import work.racka.reluct.common.settings.MultiplatformSettings

class ReluctApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KoinMain.initKoin {
            // https://github.com/InsertKoinIO/koin/issues/1188
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ReluctApplication)
            workManagerFactory()
        }

        val scope = MainScope()
        scope.launch {
            val settings: MultiplatformSettings = get()
            val canContinue = settings.onBoardingShown.firstOrNull()
            if (canContinue == true) {
                val screenTimeServices: ScreenTimeServices = get()
                screenTimeServices.startLimitsService()
            }
        }.invokeOnCompletion {
            scope.cancel()
        }

        // Setup Resume Apps worker
        ResumeAppsWork.run {
            WorkManager.getInstance(this@ReluctApplication).scheduleWork()
        }

        // Logging
        Timber.plant(Timber.DebugTree())

        // RevenueCat - Billing
        Purchases.apply {
            if (BuildConfig.DEBUG) debugLogsEnabled = true
            configure(
                configuration = PurchasesConfiguration.Builder(
                    context = this@ReluctApplication,
                    BuildConfig.revenuecat_key
                ).build()
            )
        }
    }
}
