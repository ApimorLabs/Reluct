package work.racka.reluct.common.core.navigation.destination.graphs

import com.arkivanov.essenty.parcelable.Parcelable

sealed class GoalsConfig : Parcelable {
    object Active : GoalsConfig()
    object Inactive : GoalsConfig()
}

sealed class GoalsExtrasConfig : Parcelable {
    object None : GoalsExtrasConfig()
    data class AddEdit(val goalId: String?, val defaultGoalIndex: Int?) : GoalsExtrasConfig()
    data class Details(val goalId: String?) : GoalsExtrasConfig()
}
