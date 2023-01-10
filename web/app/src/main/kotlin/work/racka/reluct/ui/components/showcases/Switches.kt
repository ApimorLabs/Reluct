package work.racka.reluct.ui.components.showcases

import mui.material.Switch
import react.FC
import react.Props
import react.dom.html.ReactHTML.div

val SwitchesShowcase = FC<Props> {
    div {
        Switch {
            defaultChecked = true
        }
        Switch {
        }
        Switch {
            disabled = true
            defaultChecked = true
        }
        Switch {
            disabled = true
        }
    }
}
