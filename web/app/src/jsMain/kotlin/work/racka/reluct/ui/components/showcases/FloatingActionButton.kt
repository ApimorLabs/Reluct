package work.racka.reluct.ui.components.showcases

import mui.icons.material.Add
import mui.material.Fab
import mui.material.FabColor
import react.FC
import react.Props

val FloatingActionButtonShowcase = FC<Props> {
    Fab {
        color = FabColor.primary

        Add()
    }
}
