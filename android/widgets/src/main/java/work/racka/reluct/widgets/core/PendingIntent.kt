package work.racka.reluct.widgets.core

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri

internal fun openDeepLinkPendingIntent(context: Context, uriString: String): PendingIntent? {
    val intent = Intent(
        Intent.ACTION_VIEW,
        uriString.toUri()
    )
    return TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(intent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}