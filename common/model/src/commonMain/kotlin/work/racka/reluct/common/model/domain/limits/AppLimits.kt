package work.racka.reluct.common.model.domain.limits

data class AppLimits(
    val packageName: String,
    val timeLimit: Long,
    val isADistractingAp: Boolean,
    val isPaused: Boolean,
    val overridden: Boolean
)