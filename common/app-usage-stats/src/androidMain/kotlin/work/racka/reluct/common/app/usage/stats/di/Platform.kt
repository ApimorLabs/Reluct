package work.racka.reluct.common.app.usage.stats.di

import android.app.usage.UsageStatsManager
import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.app.usage.stats.manager.AndroidUsageDataManager
import work.racka.reluct.common.app.usage.stats.manager.UsageDataManager

internal actual object Platform {
    actual fun module(): Module = module {
        single<UsageDataManager> {
            val context = androidApplication().applicationContext
            AndroidUsageDataManager(
                context = context,
                usageStats = context
                    .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            )
        }
    }
}
