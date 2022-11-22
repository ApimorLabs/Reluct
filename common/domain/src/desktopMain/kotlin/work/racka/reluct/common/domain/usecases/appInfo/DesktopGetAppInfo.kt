package work.racka.reluct.common.domain.usecases.appInfo

import work.racka.reluct.common.model.domain.core.Icon

internal class DesktopGetAppInfo : GetAppInfo {
    override suspend fun getAppIcon(packageName: String): Icon {
        return Icon(byteArrayOf(0))
    }

    override suspend fun getAppName(packageName: String): String {
        return packageName
    }
}
