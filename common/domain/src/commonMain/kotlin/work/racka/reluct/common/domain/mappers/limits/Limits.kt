package work.racka.reluct.common.domain.mappers.limits

import work.racka.reluct.common.database.models.LimitsDbObject
import work.racka.reluct.common.domain.usecases.appInfo.GetAppInfo
import work.racka.reluct.common.model.domain.appInfo.AppInfo
import work.racka.reluct.common.model.domain.limits.AppLimits

suspend fun LimitsDbObject.asAppLimits(getAppInfo: GetAppInfo) = AppLimits(
    appInfo = AppInfo(
        packageName = packageName,
        appName = getAppInfo.getAppName(packageName),
        appIcon = getAppInfo.getAppIcon(packageName)
    ),
    timeLimit = this.timeLimit,
    isADistractingAp = this.isADistractingAp,
    isPaused = this.isPaused,
    overridden = this.overridden
)

fun AppLimits.asLimitsDbObject() = LimitsDbObject(
    packageName = this.appInfo.packageName,
    timeLimit = this.timeLimit,
    isADistractingAp = this.isADistractingAp,
    isPaused = this.isPaused,
    overridden = this.overridden
)
