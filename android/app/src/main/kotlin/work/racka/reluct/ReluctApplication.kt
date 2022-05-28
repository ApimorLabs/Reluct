package work.racka.reluct

import android.app.Application
import com.example.reluct.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import timber.log.Timber
import work.racka.reluct.common.di.intergration.KoinMain

@HiltAndroidApp
class ReluctApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KoinMain.initKoin {
            // https://github.com/InsertKoinIO/koin/issues/1188
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ReluctApplication)
        }

        Timber.plant(Timber.DebugTree())
    }
}