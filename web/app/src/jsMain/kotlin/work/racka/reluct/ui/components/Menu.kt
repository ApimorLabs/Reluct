package work.racka.reluct.ui.components

import csstype.Color
import csstype.None
import csstype.Position
import csstype.px
import emotion.react.css
import mui.material.*
import mui.system.Box
import mui.system.sx
import react.*
import react.dom.html.ReactHTML.nav
import react.router.dom.NavLink
import react.router.useLocation
import work.racka.reluct.ui.common.Sizes
import work.racka.reluct.ui.hooks.useShowcases

val Menu = FC<Props> {
    var isOpen by useState(false)

    val showcases = useShowcases()
    val lastPathname = useLocation().pathname.substringAfterLast("/")

    Box {
        component = nav

        SwipeableDrawer {
            anchor = DrawerAnchor.left
            open = isOpen
            onOpen = { isOpen = true }
            onClose = { isOpen = false }

            Box {
                Toolbar()

                List {
                    sx { width = Sizes.Sidebar.Width }

                    for ((key, name) in showcases) {
                        NavLink {
                            to = key

                            css {
                                textDecoration = None.none
                                color = Color.currentcolor
                            }

                            ListItemButton {
                                selected = lastPathname == key

                                ListItemText {
                                    primary = ReactNode(name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    SpeedDial {
        sx {
            position = Position.absolute
            bottom = 16.px
            left = 16.px
        }

        ariaLabel = "Menu"
        icon = mui.icons.material.Menu.create()
        onClick = { isOpen = true }
    }
}