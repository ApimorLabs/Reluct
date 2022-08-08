package work.racka.reluct.common.system_service.notifications

import androidx.core.app.NotificationManagerCompat

data class NotificationChannelInfo(
    val name: String,
    val description: String,
    val channelId: String,
    val importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT
)