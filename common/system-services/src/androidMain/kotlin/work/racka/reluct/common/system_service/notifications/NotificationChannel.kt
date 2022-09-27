package work.racka.reluct.common.system_service.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import androidx.core.app.NotificationManagerCompat

data class NotificationChannelInfo(
    val name: String,
    val description: String,
    val channelId: String,
    val importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT
)

@SuppressLint("WrongConstant")
fun NotificationChannelInfo.createNotificationChannel(
    notificationManager: NotificationManagerCompat,
    showBadge: Boolean = true
) {
    val channel = NotificationChannel(
        this.channelId,
        this.name,
        this.importance
    ).apply {
        description = this.description
        setShowBadge(showBadge)
    }
    // Register the channel with the system
    notificationManager.createNotificationChannel(channel)
}