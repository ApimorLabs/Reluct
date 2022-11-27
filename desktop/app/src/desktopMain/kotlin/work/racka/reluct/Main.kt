package work.racka.reluct

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import work.racka.reluct.common.core.navigation.checks.InitialNavCheck
import work.racka.reluct.common.di.intergration.KoinMain
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.theme.Theme
import work.racka.reluct.ui.navigationComponents.core.DefaultMainAppComponent
import work.racka.reluct.ui.navigationComponents.core.MainAppComponentUI
import java.awt.Dimension

@OptIn(ExperimentalDecomposeApi::class)
fun main() {
    // Start Koin
    KoinMain.initKoin()
    val lifecycle = LifecycleRegistry()

    application {
        val initialNavCheck = remember {
            MutableValue(
                InitialNavCheck(
                    isChecking = false,
                    isOnBoardingDone = false
                )
            )
        }
        val rootComponent = DefaultMainAppComponent(
            componentContext = DefaultComponentContext(lifecycle),
            initialCheck = initialNavCheck
        )

        val windowState = rememberWindowState(
            size = DpSize(width = 1100.dp, height = 600.dp),
            placement = WindowPlacement.Floating,
            position = WindowPosition.Aligned(Alignment.Center)
        )

        // Bind the registry to the life cycle of the window
        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = stringResource(SharedRes.strings.app_name),
            icon = androidx.compose.ui.res.painterResource("icons/window_icon.svg")
        ) {
            setWindowMinSize(800, 500)

            MainAppComponentUI(
                rootComponent = rootComponent,
                themeValue = mutableStateOf(Theme.FOLLOW_SYSTEM.themeValue)
            )
        }
    }
}

fun FrameWindowScope.setWindowMinSize(width: Int, height: Int) {
    window.minimumSize = Dimension(width, height)
}
