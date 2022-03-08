package work.racka.reluct.common.model.states.about

sealed class AboutSideEffect {
    data class UpdateFound(
        val toastMessage: String
    ) : AboutSideEffect()

    data class UpdateNotFound(
        val toastMessage: String
    ) : AboutSideEffect()

    object Update : AboutSideEffect()

    object NoSideEffect : AboutSideEffect()
}
