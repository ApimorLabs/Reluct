package work.racka.reluct.ui.components

import csstype.Color
import csstype.None
import emotion.react.css
import mui.material.*
import mui.system.Box
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.nav
import react.router.dom.NavLink
import react.router.useLocation
import react.useContext
import work.racka.reluct.ui.common.Area
import work.racka.reluct.ui.common.Sizes

val Sidebar = FC<Props> {
    val showcases = useContext(ShowCasesContext)
    val lastPathname = useLocation().pathname.substringAfterLast("/")

    Box {
        component = nav
        sx { gridArea = Area.Sidebar }

        Drawer {
            variant = DrawerVariant.permanent
            anchor = DrawerAnchor.left


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
}