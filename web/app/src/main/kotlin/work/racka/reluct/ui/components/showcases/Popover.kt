package work.racka.reluct.ui.components.showcases

import csstype.px
import dom.Element
import js.core.jso
import mui.material.Button
import mui.material.ButtonVariant.contained
import mui.material.Popover
import mui.material.Typography
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.ariaDescribedBy
import react.useState

val PopoverShowcase = FC<Props> {
    var anchor by useState<Element>()

    Button {
        if (anchor != null) {
            ariaDescribedBy = "simple-popover"
        }
        variant = contained
        onClick = { anchor = it.currentTarget }

        +"Open Popover"
    }

    Popover {
        if (anchor != null) {
            id = "simple-popover"
            anchorEl = anchor
        }
        open = anchor != null
        onClose = { _, _ -> anchor = null }
        anchorOrigin = jso {
            vertical = "bottom"
            horizontal = "left"
        }

        Typography {
            sx { padding = 2.px }

            +"The content of the Popover."
        }
    }
}
