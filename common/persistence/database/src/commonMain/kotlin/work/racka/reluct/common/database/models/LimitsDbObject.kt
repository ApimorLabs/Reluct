package work.racka.reluct.common.database.models

data class LimitsDbObject(
    val packageName: String,
    val timeLimit: Long,
    val isADistractingAp: Boolean,
    val isPaused: Boolean
)
