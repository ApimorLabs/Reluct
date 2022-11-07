package work.racka.reluct.common.features.screenTime.work

import android.app.Notification
import android.content.Context
import androidx.work.*
import kotlinx.datetime.*
import work.racka.reluct.common.domain.usecases.limits.ModifyAppLimits
import work.racka.reluct.common.features.screenTime.R
import work.racka.reluct.common.features.screenTime.services.ScreenTimeServiceNotification
import work.racka.reluct.common.model.util.time.TimeConstants
import java.util.concurrent.TimeUnit

class ResumeAppsWork(
    private val modifyAppLimits: ModifyAppLimits,
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        modifyAppLimits.resumeAllApps()
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID,
            appStatsNotification(applicationContext)
        )
    }

    private fun appStatsNotification(context: Context): Notification {
        return ScreenTimeServiceNotification
            .createNotification(
                context = context,
                title = context.getString(R.string.resume_apps_notif_title),
                content = context.getString(R.string.resume_apps_notif_content),
                onNotificationClick = { null }
            )
    }

    companion object {
        const val NOTIFICATION_ID = 20221107
        const val WORKER_NAME = "resume_apps_work"

        private fun getMinutesRemaining(): Long {
            val timeZone = TimeZone.currentSystemDefault()
            val now = Clock.System.now()
            val dateTime = now.toLocalDateTime(timeZone)
            val endOfDay = LocalDateTime(
                dateTime.year,
                dateTime.month,
                dateTime.dayOfMonth,
                TimeConstants.DAILY_HOURS - 1,
                TimeConstants.HOURLY_MINUTES_SECONDS - 1,
                TimeConstants.HOURLY_MINUTES_SECONDS - 1,
                TimeConstants.MINUTE_MILLIS - 1
            ).toInstant(timeZone)
            val timeDiff = endOfDay - now
            return timeDiff.inWholeMinutes.let { if (it < 0) 0L else it }
        }

        private val request = PeriodicWorkRequestBuilder<ResumeAppsWork>(1, TimeUnit.DAYS)
            .setInitialDelay(getMinutesRemaining(), TimeUnit.MINUTES)
            .addTag("resume_apps_work")
            .build()

        fun WorkManager.scheduleWork(): Operation {
            return enqueueUniquePeriodicWork(WORKER_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
        }

        fun WorkManager.cancelWork(): Operation {
            return cancelUniqueWork(WORKER_NAME)
        }
    }
}
