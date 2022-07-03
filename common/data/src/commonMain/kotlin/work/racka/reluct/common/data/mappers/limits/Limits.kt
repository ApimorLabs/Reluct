package work.racka.reluct.common.data.mappers.limits

import work.racka.reluct.common.database.models.LimitsDbObject
import work.racka.reluct.common.model.domain.limits.Limits

fun LimitsDbObject.asLimits() = Limits(
    packageName = this.packageName,
    timeLimit = this.timeLimit,
    isADistractingAp = this.isADistractingAp,
    isPaused = this.isPaused,
    overridden = this.overridden
)

fun Limits.asLimitsDbObject() = LimitsDbObject(
    packageName = this.packageName,
    timeLimit = this.timeLimit,
    isADistractingAp = this.isADistractingAp,
    isPaused = this.isPaused,
    overridden = this.overridden
)