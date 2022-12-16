package work.racka.reluct.ui.components.showcases

import mui.material.*
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.aria.ariaLabel
import react.dom.html.ReactHTML.fieldset
import react.dom.html.ReactHTML.legend

val RadioButtonsShowcase = FC<Props> {
    FormControl {
        component = fieldset

        FormLabel {
            component = legend

            +"Gender"
        }

        RadioGroup {
            ariaLabel = "gender"
            defaultValue = "female"
            name = "radio-buttons-group"

            FormControlLabel {
                value = "female"
                control = Radio.create()
                label = ReactNode("Female")
            }
            FormControlLabel {
                value = "male"
                control = Radio.create()
                label = ReactNode("Male")
            }
            FormControlLabel {
                value = "other"
                control = Radio.create()
                label = ReactNode("Other")
            }
        }
    }
}
