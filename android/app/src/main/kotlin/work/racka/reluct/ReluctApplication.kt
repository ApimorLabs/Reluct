package work.racka.reluct

import android.app.Application
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import timber.log.Timber
import work.racka.reluct.common.di.intergration.KoinMain
import work.racka.reluct.common.features.screen_time.services.ScreenTimeServices

class ReluctApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KoinMain.initKoin {
            // https://github.com/InsertKoinIO/koin/issues/1188
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ReluctApplication)
        }
        //ServiceStarter.startService(applicationContext)
        val screenTimeServices: ScreenTimeServices by inject()
        screenTimeServices.startLimitsService()

        Timber.plant(Timber.DebugTree())
    }
}