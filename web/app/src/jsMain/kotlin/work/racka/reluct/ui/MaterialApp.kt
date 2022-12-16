package work.racka.reluct.ui

import csstype.Auto
import csstype.Display
import csstype.GridTemplateAreas
import csstype.array
import mui.material.useMediaQuery
import mui.system.Box
import mui.system.sx
import react.FC
import react.Props
import react.router.dom.HashRouter
import work.racka.reluct.ui.common.Area
import work.racka.reluct.ui.common.Sizes
import work.racka.reluct.ui.components.*

val MaterialApp = FC<Props> {
    val mobileNode = useMediaQuery("(max-width:960px)")

    HashRouter {
        ShowcasesModule {
            ThemeModule {
                Box {
                    sx {
                        display = Display.grid
                        gridTemplateRows = array(Sizes.Header.Height, Auto.auto)
                        gridTemplateColumns = array(Sizes.Sidebar.Width, Auto.auto)
                        gridTemplateAreas = GridTemplateAreas(
                            array(Area.Header, Area.Header),
                            if (mobileNode) array(Area.Content, Area.Content)
                            else array(Area.Sidebar, Area.Content)
                        )
                    }
                    
                    Header()
                    if (mobileNode) Menu() else Sidebar()
                    Content()
                }
            }
        }
    }
}