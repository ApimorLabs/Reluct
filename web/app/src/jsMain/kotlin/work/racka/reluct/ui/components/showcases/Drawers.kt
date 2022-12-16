package work.racka.reluct.ui.components.showcases

import mui.material.Button
import mui.material.ButtonVariant.outlined
import mui.material.Drawer
import mui.material.Toolbar
import react.FC
import react.Props
import react.useState

val DrawersShowcase = FC<Props> {
    var isOpen by useState(false)

    Button {
        variant = outlined
        onClick = { isOpen = true }
        +"Open drawer"
    }

    Drawer {
        open = isOpen

        Toolbar()

        Button {
            variant = outlined
            onClick = { isOpen = false }
            +"Close drawer"
        }
    }
}