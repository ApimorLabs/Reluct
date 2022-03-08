package work.racka.reluct.di

import android.app.usage.UsageStatsManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import work.racka.reluct.data.local.usagestats.UsageDataManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UsageStatsModule {

    @Singleton
    @Provides
    fun providesUsageStatsManager(
        @ApplicationContext context: Context
    ): UsageStatsManager = context
        .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    @Singleton
    @Provides
    fun providesUsageData(
        @ApplicationContext context: Context,
        usageStatsManager: UsageStatsManager
    ): UsageDataManager = UsageDataManager(context, usageStatsManager)
}