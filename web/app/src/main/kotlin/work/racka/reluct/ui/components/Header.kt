package work.racka.reluct.ui.components

import csstype.integer
import csstype.number
import csstype.px
import csstype.rgba
import kotlinx.browser.window
import mui.icons.material.Brightness4
import mui.icons.material.Brightness7
import mui.icons.material.GitHub
import mui.icons.material.MenuBook
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.*
import react.dom.aria.AriaHasPopup
import react.dom.aria.ariaHasPopup
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML.div
import react.router.useLocation
import work.racka.reluct.ui.common.Area
import work.racka.reluct.ui.common.Themes

val Header = FC<Props> {
    var theme by useContext(ThemeContext)
    val lastPathname = useLocation().pathname.substringAfterLast("/")

    AppBar {
        position = AppBarPosition.fixed
        sx {
            gridArea = Area.Header
            zIndex = integer(1_500)
        }

        Toolbar {
            Avatar {
                src = "app_icon_alt.svg"
                variant = AvatarVariant.square
                sx {
                    backgroundColor = rgba(0, 0, 0, 0.0)
                    marginRight = 16.px
                }
            }
            Typography {
                sx { flexGrow = number(1.0) }
                variant = TypographyVariant.h6
                noWrap = true
                component = div

                +"Reluct"
            }

            Tooltip {
                title = ReactNode("Theme")

                Switch {
                    icon = Brightness7.create()
                    checkedIcon = Brightness4.create()
                    checked = theme == Themes.Dark
                    ariaLabel = "theme"

                    onChange = { _, checked ->
                        theme = if (checked) Themes.Dark else Themes.Light
                    }
                }
            }

            Tooltip {
                title = ReactNode("Read Documentation")

                IconButton {
                    ariaLabel = "official documentation"
                    ariaHasPopup = AriaHasPopup.`false`
                    size = Size.large
                    color = IconButtonColor.inherit
                    onClick = { window.location.href = "https://mui.com/components/$lastPathname" }

                    MenuBook()
                }
            }

            Tooltip {
                title = ReactNode("View Sources")

                IconButton {
                    ariaHasPopup = AriaHasPopup.`false`
                    size = Size.large
                    color = IconButtonColor.inherit
                    onClick = {
                        var name = lastPathname
                            .split("-")
                            .asSequence()
                            .map { item -> item.replaceFirstChar { it.titlecase() } }
                            .reduce { accumulator, word -> accumulator.plus(word) }

                        if (name.isNotEmpty()) {
                            name += ".kt"
                        }

                        window.location.href =
                            "https://github.com/karakum-team/kotlin-mui-showcase/blob/main/" +
                            "src/main/kotlin/team/karakum/components/showcases/$name"
                    }

                    GitHub()
                }
            }
        }
    }
}
