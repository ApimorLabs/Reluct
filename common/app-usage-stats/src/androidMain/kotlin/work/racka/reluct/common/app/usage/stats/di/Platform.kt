package work.racka.reluct.common.app.usage.stats.di

import android.app.usage.UsageStatsManager
import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManagerImpl

internal actual object Platform {
    actual fun module(): Module = module {
        factory<UsageDataManager> {
            val context = androidApplication().applicationContext
            UsageDataManagerImpl(
                context = context,
                usageStats = context
                    .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            )
        }
    }
}
