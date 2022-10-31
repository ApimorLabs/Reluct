package work.racka.reluct.common.services.notifications

import work.racka.reluct.common.model.domain.core.Icon

data class NotificationData(
    val iconProvider: Icon?,
    val title: String,
    val content: String,
    val notificationId: Int,
    val notificationTag: String,
    val category: String? = null
)
