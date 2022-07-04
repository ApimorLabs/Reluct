package work.racka.reluct.common.data.mappers.limits

import work.racka.reluct.common.database.models.LimitsDbObject
import work.racka.reluct.common.model.domain.limits.AppLimits

fun LimitsDbObject.asAppLimits() = AppLimits(
    packageName = this.packageName,
    timeLimit = this.timeLimit,
    isADistractingAp = this.isADistractingAp,
    isPaused = this.isPaused,
    overridden = this.overridden
)

fun AppLimits.asLimitsDbObject() = LimitsDbObject(
    packageName = this.packageName,
    timeLimit = this.timeLimit,
    isADistractingAp = this.isADistractingAp,
    isPaused = this.isPaused,
    overridden = this.overridden
)