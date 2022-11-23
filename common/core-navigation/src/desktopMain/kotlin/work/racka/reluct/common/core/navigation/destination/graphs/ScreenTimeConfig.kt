package work.racka.reluct.common.core.navigation.destination.graphs

import com.arkivanov.essenty.parcelable.Parcelable

sealed class ScreenTimeConfig : Parcelable {
    object StatsAndLimits : ScreenTimeConfig()
}

sealed class AppScreenTimeConfig : Parcelable {
    object None : AppScreenTimeConfig()
    data class App(val packageName: String?) : AppScreenTimeConfig()
}
