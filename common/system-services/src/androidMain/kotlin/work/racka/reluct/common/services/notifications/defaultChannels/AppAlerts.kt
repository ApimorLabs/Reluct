package work.racka.reluct.common.services.notifications.defaultChannels

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import work.racka.reluct.common.services.notifications.NotificationChannelInfo
import work.racka.reluct.common.system_services.R

fun getAppAlertsChannel(context: Context) = NotificationChannelInfo(
    name = context.getString(R.string.app_alerts_notif_channel_title),
    description = context.getString(R.string.app_alerts_notif_channel_desc),
    channelId = "app_alerts_channel",
    importance = NotificationManagerCompat.IMPORTANCE_HIGH
)
