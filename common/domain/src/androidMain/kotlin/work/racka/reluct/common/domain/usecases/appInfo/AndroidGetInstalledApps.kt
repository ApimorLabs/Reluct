package work.racka.reluct.common.domain.usecases.appInfo

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.common.domain.util.hasMainActivity
import work.racka.reluct.common.model.domain.appInfo.AppInfo

internal class AndroidGetInstalledApps(
    private val context: Context,
    private val getAppInfo: GetAppInfo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GetInstalledApps {
    private val packageManager = context.packageManager

    @SuppressLint("QueryPermissionsNeeded")
    override suspend fun invoke(): ImmutableList<AppInfo> = withContext(dispatcher) {
        val installedApps = packageManager.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getInstalledApplications(
                    PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
                )
            } else {
                @Suppress("DEPRECATION")
                getInstalledApplications(PackageManager.GET_META_DATA)
            }
        }
        installedApps.filter {
            hasMainActivity(context = context, packageName = it.packageName)
        }.map {
            AppInfo(
                packageName = it.packageName,
                appName = getAppInfo.getAppName(it.packageName),
                appIcon = getAppInfo.getAppIcon(it.packageName)
            )
        }
            .sortedBy { it.appName }
            .toImmutableList()
    }
}
