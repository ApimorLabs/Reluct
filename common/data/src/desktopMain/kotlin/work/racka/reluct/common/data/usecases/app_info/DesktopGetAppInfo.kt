package work.racka.reluct.common.data.usecases.app_info

import work.racka.reluct.common.model.domain.usagestats.Icon

class DesktopGetAppInfo : GetAppInfo {
    override fun getAppIcon(packageName: String): Icon {
        return Icon(byteArrayOf(0))
    }

    override fun getAppName(packageName: String): String {
        return packageName
    }
}