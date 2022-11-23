package work.racka.reluct.common.core.navigation.destination

import com.arkivanov.essenty.parcelable.Parcelable

sealed class AppNavConfig : Parcelable {
    object OnBoarding : AppNavConfig() // Should hide the nav rail
    object Dashboard : AppNavConfig()
    object Tasks : AppNavConfig() // Multipane - 2nd pane always active
    object ScreenTime : AppNavConfig() // Multipane - adaptive
    object Goals : AppNavConfig() // Multipane - 2nd pane always active
    object Settings : AppNavConfig() // Should preferably be a dialog or a new Window
}
