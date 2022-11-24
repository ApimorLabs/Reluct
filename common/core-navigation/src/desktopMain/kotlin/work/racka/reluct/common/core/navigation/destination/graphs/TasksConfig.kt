package work.racka.reluct.common.core.navigation.destination.graphs

import com.arkivanov.essenty.parcelable.Parcelable

sealed class TasksConfig : Parcelable {
    object Completed : TasksConfig()
    object Pending : TasksConfig()
    object Search : TasksConfig()
    object Statistics : TasksConfig()
}

sealed class TasksExtraConfig : Parcelable {
    object None : TasksExtraConfig()
    data class AddEdit(val taskId: String?) : TasksExtraConfig()
    data class Details(val taskId: String?) : TasksExtraConfig()
}
