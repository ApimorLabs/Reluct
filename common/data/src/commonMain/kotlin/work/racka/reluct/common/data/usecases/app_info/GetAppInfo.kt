package work.racka.reluct.common.data.usecases.app_info

import work.racka.reluct.common.model.domain.usagestats.Icon

interface GetAppInfo {
    fun getAppIcon(packageName: String): Icon
    fun getAppName(packageName: String): String
}