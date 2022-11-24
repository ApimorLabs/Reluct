package work.racka.reluct.common.core.navigation.checks

/**
 * Correct data is only guaranteed after [isChecking] is false. Please don't rely on this data
 * before that is complete.
 *
 * @param isChecking Still performing the check since its async. You are safe to access the other
 * params after [isChecking] is false
 * @param isOnBoardingDone Shows if on boarding flow was already done by the user
 * @param showChangeLog Shows if you should provide a changelog to user or not
 */
data class InitialNavCheck(
    val isChecking: Boolean = true,
    val isOnBoardingDone: Boolean = false,
    val showChangeLog: Boolean = false
)
