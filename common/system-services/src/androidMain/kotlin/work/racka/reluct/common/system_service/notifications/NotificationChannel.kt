package work.racka.reluct.common.system_service.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.os.Build
import androidx.core.app.NotificationManagerCompat

data class NotificationChannelInfo(
    val name: String,
    val description: String,
    val channelId: String,
    val importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT
)

@SuppressLint("WrongConstant")
fun NotificationChannelInfo.createNotificationChannel(
    notificationManager: NotificationManagerCompat
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            this.channelId,
            this.name,
            this.importance
        ).apply { description = this.description }
        // Register the channel with the system
        notificationManager.createNotificationChannel(channel)
    }
}