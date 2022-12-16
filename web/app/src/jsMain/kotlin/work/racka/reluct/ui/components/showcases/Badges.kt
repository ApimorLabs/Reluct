package work.racka.reluct.ui.components.showcases

import mui.icons.material.Mail
import mui.material.Badge
import mui.material.BadgeColor
import react.FC
import react.Props
import react.ReactNode

val BadgesShowcase = FC<Props> {
    Badge {
        color = BadgeColor.primary
        badgeContent = ReactNode("5")

        Mail()
    }
}