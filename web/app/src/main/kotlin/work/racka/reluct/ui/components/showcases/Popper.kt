package work.racka.reluct.ui.components.showcases

import csstype.AlignItems.Companion.flexEnd
import csstype.JustifyContent.Companion.center
import csstype.px
import dom.Element
import mui.material.*
import mui.material.GridDirection.column
import mui.system.responsive
import mui.system.sx
import popper.core.Placement
import popper.core.Placement.*
import react.FC
import react.Props
import react.dom.events.MouseEvent
import react.dom.html.ReactHTML.br
import react.useState
import work.racka.reluct.ui.common.xs

val PopperShowcase = FC<Props> {
    var isOpen by useState(false)
    var anchor by useState<Element>()
    var place by useState<Placement>()

    fun MouseEvent<*, *>.handleClick(newPlacement: Placement) {
        isOpen = place != newPlacement || !isOpen
        place = newPlacement
        anchor = currentTarget
    }

    Box {
        sx { width = 500.px }

        Popper {
            open = isOpen
            anchorEl = anchor
            placement = place

            Typography {
                sx { padding = 2.px }

                +"The content of the Popper."
            }
        }

        Grid {
            container = true
            sx { justifyContent = center }

            Grid {
                item = true
                Button {
                    onClick = { it.handleClick(topStart) }

                    +"$topStart"
                }
                Button {
                    onClick = { it.handleClick(top) }

                    +"$top"
                }
                Button {
                    onClick = { it.handleClick(topEnd) }

                    +"$topEnd"
                }
            }
        }
        Grid {
            container = true
            sx { justifyContent = center }

            Grid {
                item = true
                xs = 6
                Button {
                    onClick = { it.handleClick(leftStart) }

                    +"$leftStart"
                }
                br()
                Button {
                    onClick = { it.handleClick(left) }

                    +"$left"
                }
                br()
                Button {
                    onClick = { it.handleClick(leftEnd) }

                    +"$leftEnd"
                }
            }
            Grid {
                sx { alignItems = flexEnd }
                item = true
                container = true
                xs = 6
                direction = responsive(column)

                Grid {
                    item = true

                    Button {
                        onClick = { it.handleClick(rightStart) }

                        +"$rightStart"
                    }
                }
                Grid {
                    item = true

                    Button {
                        onClick = { it.handleClick(right) }

                        +"$right"
                    }
                }
                Grid {
                    item = true
                    Button {
                        onClick = { it.handleClick(rightEnd) }

                        +"$rightEnd"
                    }
                }
            }
        }
        Grid {
            container = true
            sx { justifyContent = center }

            Grid {
                item = true
                Button {
                    onClick = { it.handleClick(bottomStart) }

                    +"$bottomStart"
                }
                Button {
                    onClick = { it.handleClick(bottom) }

                    +"$bottom"
                }
                Button {
                    onClick = { it.handleClick(bottomEnd) }

                    +"$bottomEnd"
                }
            }
        }
    }
}
