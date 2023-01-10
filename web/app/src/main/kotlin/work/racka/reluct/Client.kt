package work.racka.reluct

import browser.document
import dom.html.HTML
import dom.html.createElement
import react.create
import react.dom.client.createRoot
import work.racka.reluct.ui.MaterialApp

fun main() {
    val container = document.createElement(HTML.div)
        .also { document.body.appendChild(it) }
    createRoot(container).render(MaterialApp.create())
}
