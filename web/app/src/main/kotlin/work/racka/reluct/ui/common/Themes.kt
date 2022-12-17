package work.racka.reluct.ui.common

import js.core.jso
import mui.material.PaletteMode.dark
import mui.material.PaletteMode.light
import mui.material.styles.createTheme

object Themes {
    val Light = createTheme(
        options = jso {
            palette = jso {
                mode = light
                primary = jso {
                    main = "#6167FF"
                }
                secondary = jso {
                    main = "#6167FF"
                }
                background = jso {
                    default = "#f1f0f5"
                    paper = "#f1f0f5"
                }
                error = jso {
                    main = "#FF8989"
                }
            }
            typography = jso {
                fontFamily = "Lexend Deca"
            }
        }
    )

    val Dark = createTheme(
        options = jso {
            palette = jso {
                mode = dark
                primary = jso {
                    main = "#6167FF"
                }
                secondary = jso {
                    main = "#6167FF"
                }
                background = jso {
                    default = "#121212"
                    paper = "#121212"
                }
                error = jso {
                    main = "#FF8989"
                }
            }
            typography = jso {
                fontFamily = "Lexend Deca"
            }
        }
    )
}
