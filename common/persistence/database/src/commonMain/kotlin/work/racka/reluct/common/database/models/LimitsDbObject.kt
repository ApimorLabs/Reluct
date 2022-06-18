package work.racka.reluct.common.database.models

data class LimitsDbObject(
    private val packageName: String,
    private val timeLimit: Long,
    private val isADistractingAp: Boolean,
    private val isPaused: Boolean
)
