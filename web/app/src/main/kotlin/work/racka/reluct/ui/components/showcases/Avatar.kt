package work.racka.reluct.ui.components.showcases

import mui.material.Avatar
import mui.material.AvatarGroup
import react.FC
import react.Props

val AvatarsShowcase = FC<Props> {
    AvatarGroup {
        max = 4

        Avatar {
            alt = "Remy Sharp"
            src = "avatar.png"
        }
        Avatar {
            alt = "Travis Howard"
            +"TH"
        }
        Avatar {
            alt = "Cindy Baker"
        }
        Avatar {
            alt = "Agnes Walker"
            +"AW"
        }
        Avatar {
            alt = "Trevor Henderson"
        }
    }
}
