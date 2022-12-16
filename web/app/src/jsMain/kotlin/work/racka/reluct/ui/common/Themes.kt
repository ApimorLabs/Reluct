package work.racka.reluct.ui.common

import js.core.jso
import mui.material.PaletteMode.dark
import mui.material.PaletteMode.light
import mui.material.styles.createTheme

object Themes {
    val Light = createTheme(
        options = jso {
            palette = jso { mode = light }
        }
    )

    val Dark = createTheme(
        options = jso {
            palette = jso { mode = dark }
        }
    )
}
