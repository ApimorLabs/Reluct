package work.racka.reluct.ui.components.showcases

import js.core.jso
import mui.material.Menu
import mui.material.MenuItem
import mui.material.PopoverReference
import mui.material.Typography
import react.FC
import react.Props
import react.dom.events.MouseEvent
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML.div
import react.useState

val MenusShowcase = FC<Props> {
    var point by useState<Point>()

    val handleContextMenu = { event: MouseEvent<*, *> ->
        event.preventDefault()
        point = if (point == null) {
            Point(
                x = event.clientX - 2,
                y = event.clientY - 4,
            )
        } else {
            null
        }
    }

    val handleClose: MouseEventHandler<*> = { point = null }

    div {
        onContextMenu = handleContextMenu

        Typography {
            +"""
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse malesuada 
                    lacus ex, sit amet blandit leo lobortis eget.
            """.trimIndent()
        }
        Menu {
            open = point != null
            onClose = handleClose

            anchorReference = PopoverReference.anchorPosition
            anchorPosition = if (point != null) {
                jso {
                    top = point!!.y
                    left = point!!.x
                }
            } else {
                undefined
            }

            MenuItem {
                onClick = handleClose
                +"Copy"
            }
            MenuItem {
                onClick = handleClose
                +"Print"
            }
            MenuItem {
                onClick = handleClose
                +"Highlight"
            }
            MenuItem {
                onClick = handleClose
                +"Email"
            }
        }
    }
}

private data class Point(
    val x: Double,
    val y: Double,
)
