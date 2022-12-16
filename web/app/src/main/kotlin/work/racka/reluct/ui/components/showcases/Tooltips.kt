package work.racka.reluct.ui.components.showcases

import mui.material.IconButton
import mui.material.SpeedDialIcon
import mui.material.Tooltip
import react.FC
import react.Props
import react.ReactNode

val TooltipsShowcase = FC<Props> {
    Tooltip {
        title = ReactNode("Speed Dial")

        IconButton {
            SpeedDialIcon()
        }
    }
}
