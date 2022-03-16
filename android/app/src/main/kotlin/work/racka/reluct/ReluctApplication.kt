package work.racka.reluct

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.reluct.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import timber.log.Timber
import work.racka.reluct.android.compose.theme.Theme
import work.racka.reluct.common.integration.containers.settings.AppSettings
import work.racka.reluct.common.integration.di.KoinMain
import work.racka.reluct.common.model.states.settings.SettingsSideEffect

@HiltAndroidApp
class ReluctApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KoinMain.initKoin {
            // https://github.com/InsertKoinIO/koin/issues/1188
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ReluctApplication)
        }

        val settings by inject<AppSettings>()

        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        scope.launch {
            settings.host.sideEffect.collectLatest { effect ->
                when (effect) {
                    is SettingsSideEffect.ApplyThemeOption -> {
                        if (effect.themeValue != Theme.MATERIAL_YOU.themeValue) {
                            AppCompatDelegate.setDefaultNightMode(effect.themeValue)
                        } else {
                            AppCompatDelegate.setDefaultNightMode(
                                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                            )
                        }
                    }
                    else -> {}
                }
            }
        }

        Timber.plant(Timber.DebugTree())
    }
}