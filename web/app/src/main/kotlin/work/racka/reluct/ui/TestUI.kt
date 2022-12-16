package work.racka.reluct.ui

import mui.icons.material.PhotoCamera
import mui.material.*
import mui.material.styles.TypographyVariant
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.main
import work.racka.reluct.ui.common.color
import work.racka.reluct.ui.components.ThemeModule

val TestUi = FC<Props> {
    ThemeModule {
        AppBar {
            Toolbar {
                PhotoCamera()
                Typography {
                    variant = TypographyVariant.h6
                    +"Photo Album"
                }
            }
        }

        main {
            div {
                Container {
                    //maxWidth = "sm".asDynamic()

                    Typography {
                        variant = TypographyVariant.h2
                        align = TypographyAlign.center
                        color = "textPrimary"

                        gutterBottom = true

                        +"Photo Album"
                    }

                    Typography {
                        variant = TypographyVariant.h5
                        align = TypographyAlign.center
                        color = "textSecondary"

                        paragraph = true

                        +"""
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse malesuada 
                            lacus ex, sit amet blandit leo lobortis eget.
                        """.trimIndent()
                    }
                }
            }
        }
    }
}