package work.racka.reluct.ui.components.showcases

import mui.icons.material.FormatAlignCenter
import mui.icons.material.FormatAlignJustify
import mui.icons.material.FormatAlignLeft
import mui.icons.material.FormatAlignRight
import mui.material.ToggleButton
import mui.material.ToggleButtonGroup
import react.FC
import react.Props
import react.dom.aria.ariaLabel
import react.useState

val ToggleButtonShowcase = FC<Props> {
    var alignment by useState("left")

    ToggleButtonGroup {
        exclusive = true
        value = alignment
        onChange = { _, newAlignment -> alignment = newAlignment }
        ariaLabel = "text alignment"

        ToggleButton {
            value = "left"
            ariaLabel = "left aligned"

            FormatAlignLeft()
        }
        ToggleButton {
            value = "center"
            ariaLabel = "centered"

            FormatAlignCenter()
        }
        ToggleButton {
            value = "right"
            ariaLabel = "right aligned"

            FormatAlignRight()
        }
        ToggleButton {
            disabled = true
            value = "justify"
            ariaLabel = "justified"

            FormatAlignJustify()
        }
    }
}
